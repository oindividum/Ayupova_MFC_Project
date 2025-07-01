package org.example.popitka2.controller.ForUser;

import org.example.popitka2.model.Appointment;
import org.example.popitka2.service.MoskvichCardService;
import org.example.popitka2.repository.AppointmentRepository;
import org.example.popitka2.repository.UserRepository;
import org.example.popitka2.service.AppointmentService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;


import org.example.popitka2.model.User;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private final AppointmentService appointmentService;
    private final AppointmentRepository appointmentRepository;
    private final MoskvichCardService cardService;

    public UserController(UserRepository userRepository, AppointmentService appointmentService,
                          AppointmentRepository appointmentRepository, MoskvichCardService cardService) {
        this.userRepository = userRepository;
        this.appointmentService = appointmentService;
        this.appointmentRepository = appointmentRepository;
        this.cardService = cardService;
    }

    @PostMapping("/book_appointment")
    public String bookAppointment(@RequestParam String fullName,
                                  @RequestParam LocalDate birthDate,
                                  @RequestParam String phoneNumber,
                                  @RequestParam String doctor,
                                  @RequestParam LocalDate appointmentDate,
                                  @RequestParam String appointmentTime,
                                  @RequestParam(required = false) String reason,
                                  Model model) {

        LocalDateTime appointmentDateTime = LocalDateTime.parse(appointmentDate + "T" + appointmentTime);
        Appointment appointment = new Appointment(fullName, birthDate, phoneNumber, doctor, appointmentDateTime, "Записан", reason, "Поликлиника");
        appointmentRepository.save(appointment);
        model.addAttribute("successMessage", "✅ Ваша заявка успешно отправлена.");
        return "book_appointment";
    }

    @GetMapping("/book_appointment")
    public String showBookingPage() {
        return "book_appointment";
    }

    @GetMapping("/moskvich_card")
    public String showMoskvichCardPage(Principal principal, Model model) {
        String username = principal.getName();
        String requestNumber = cardService.getRequestNumberByUsername(username);

        System.out.println("🔹 requestNumber (MoskvichCardController): " + requestNumber);

        model.addAttribute("username", username);
        model.addAttribute("requestNumber", requestNumber);

        return "moskvich_card";
    }


    @PostMapping("/cancel_appointment")
    public String cancelAppointment(@RequestParam Long appointmentId) {
        appointmentRepository.deleteById(appointmentId);
        return "redirect:/user/profile";
    }

    @GetMapping("/profile")
    public String showProfilePage(Authentication authentication, Model model) {
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        List<Appointment> allAppointments = appointmentRepository.findByPatientName(user.getUsername());
        List<Appointment> upcomingAppointments = allAppointments.stream()
                .filter(a -> a.getDate().isAfter(LocalDateTime.now()))
                .toList();
        List<Appointment> pastAppointments = allAppointments.stream()
                .filter(a -> a.getDate().isBefore(LocalDateTime.now()))
                .toList();

        Optional<Appointment> nearestAppointmentOpt = upcomingAppointments.stream()
                .sorted(Comparator.comparing(Appointment::getDate)
                        .thenComparing(Appointment::getDoctor))
                .findFirst();

        String nearestAppointment = nearestAppointmentOpt.map(a ->
                        "👉 Ближайший приём: " + a.getDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm")) + " у " + a.getDoctor())
                .orElse("");

        model.addAttribute("nearestAppointment", nearestAppointment);

        // Проверка данных карты москвича
        String cardStatus = cardService.getCardStatusByUsername(user.getUsername());
        if (cardStatus == null || cardStatus.isEmpty()) {
            cardStatus = "❌ Карта не оформлена";
        }
        model.addAttribute("cardStatus", cardStatus);

        String requestNumber = cardService.getRequestNumberByUsername(user.getUsername());
        if (requestNumber == null || requestNumber.isEmpty()) {
            requestNumber = "❌ Номер карты отсутствует";
        }
        model.addAttribute("requestNumber", requestNumber);

        model.addAttribute("user", user);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("appointments", upcomingAppointments);
        model.addAttribute("pastAppointments", pastAppointments);

        return "user_profile";
    }


@GetMapping("/user/dashboard")
public String showUserDashboard(Principal principal, Model model) {
    //Получаем пользователя
    User user = userRepository.findByUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("❌ Ошибка: Пользователь не найден!"));

    // Получаем все записи на приём
    List<Appointment> upcomingAppointments = appointmentRepository.findByPatientName(user.getUsername())
            .stream().filter(a -> a.getDate().isAfter(LocalDateTime.now())).toList();

    List<Appointment> pastAppointments = appointmentRepository.findByPatientName(user.getUsername())
            .stream().filter(a -> a.getDate().isBefore(LocalDateTime.now())).toList();

    // Проверяем статус карты москвича
    String cardStatus = cardService.getCardStatusByUsername(user.getUsername());
    if (cardStatus == null || cardStatus.isEmpty()) {
        cardStatus = "❌ Карта не оформлена";
    }
    model.addAttribute("cardStatus", cardStatus);

    // Проверяем номер заявки
    String requestNumber = cardService.getRequestNumberByUsername(user.getUsername());
    if (requestNumber == null || requestNumber.isEmpty()) {
        requestNumber = "❌ Номер карты отсутствует";
    }
    model.addAttribute("requestNumber", requestNumber);

    // Добавляем атрибуты в модель
    model.addAttribute("user", user);
    model.addAttribute("username", user.getUsername());
    model.addAttribute("appointments", upcomingAppointments);
    model.addAttribute("pastAppointments", pastAppointments);

    return "user_dashboard";
}


    private List<Appointment> getUserAppointments(User user, boolean upcoming) {
        return appointmentRepository.findByPatientName(user.getUsername())
                .stream()
                .filter(a -> upcoming ? a.getDate().isAfter(LocalDateTime.now()) : a.getDate().isBefore(LocalDateTime.now()))
                .toList();
    }

    @GetMapping("/edit_profile")
    public String editProfilePage(Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        model.addAttribute("user", user);
        return "edit_profile";
    }


    @PostMapping("/update_profile")
    public String updateProfile(@RequestParam String username,
                                @RequestParam String email,
                                @RequestParam String phoneNumber,
                                @RequestParam LocalDate birthDate,
                                Principal principal) {

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        System.out.println("🔹 Роль пользователя перед обновлением: " + user.getRole());

        String oldUsername = user.getUsername();

        user.setUsername(username);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setBirthDate(birthDate);

        userRepository.save(user);

        List<Appointment> appointments = appointmentRepository.findByPatientName(oldUsername);
        for (Appointment appointment : appointments) {
            appointment.setPatientName(username);
        }
        appointmentRepository.saveAll(appointments);

        System.out.println("🔹 Перенаправление на: /user/profile?success=updated");

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("✅ Профиль успешно обновлён для пользователя: " + user.getUsername());

        return "redirect:/user/profile?success=updated";
    }


    @GetMapping("/user/book_appointment")
    public String showBookingPage(Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        model.addAttribute("username", user.getUsername()); //Передаём имя пользователя
        return "book_appointment";
    }

    @GetMapping("/services")
    public String showServicesPage() {
        return "development"; // Открывает `development.html`
    }

}