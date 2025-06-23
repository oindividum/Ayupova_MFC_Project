package org.example.popitka2.controller.ForMfc_Emplyee;

import org.example.popitka2.repository.AppointmentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime; // Для работы с датами

import org.example.popitka2.service.AppointmentService;
import org.example.popitka2.service.MoskvichCardService;
import org.example.popitka2.model.Appointment;

import java.security.Principal;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;

import org.example.popitka2.repository.UserRepository;
import org.example.popitka2.model.User;
import org.example.popitka2.model.Role;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/mfc")
public class MfcController {
    private final AppointmentService appointmentService;
    private final MoskvichCardService cardService;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;


    public MfcController(AppointmentService appointmentService, MoskvichCardService cardService, UserRepository userRepository, AppointmentRepository appointmentRepository) {
        this.appointmentService = appointmentService;
        this.cardService = cardService;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // Список записей на прием
    @GetMapping("/appointments")
    public String viewAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getAllAppointments()); // Передаем записи
        return "mfc_appointments";
    }


    @PostMapping("/appointments/book")
    public String bookAppointment(@RequestParam String patientName,
                                  @RequestParam String birthDate,
                                  @RequestParam String phoneNumber,
                                  @RequestParam String doctor,
                                  @RequestParam LocalDateTime appointmentDate,
                                  @RequestParam(required = false) String reason,
                                  @RequestParam String place) {

        LocalDate parsedBirthDate = LocalDate.parse(birthDate);
        System.out.println("📌 Метод bookAppointment() вызван! Данные: " + patientName);

        appointmentService.bookAppointment(patientName, parsedBirthDate, phoneNumber, doctor, appointmentDate, reason, place);

        return "redirect:/mfc/appointments";
    }


    // Отметить завершение приема
    @PostMapping("/appointments/{id}/complete")
    public String completeAppointment(@PathVariable Long id) {
        appointmentService.completeAppointment(id);
        return "redirect:/mfc/appointments";
    }

    // Редактирование записи сотрудником МФЦ
    @PostMapping("/appointments/{id}/edit")
    public String editAppointment(@PathVariable Long id,
                                  @RequestParam String patientName,
                                  @RequestParam String doctor,
                                  @RequestParam LocalDateTime date,
                                  @RequestParam String place) {
        appointmentService.updateAppointment(id, patientName, doctor, date, place);
        return "redirect:/mfc/appointments";
    }

    // Список заявок на карту москвича
    @GetMapping("/moskvich_card")
    public String viewCardRequests(Model model) {
        model.addAttribute("cardRequests", cardService.getAllRequests());
        return "moskvich_card_success";
    }

    @GetMapping("/moskvich_card/success")
    public String viewAllCardRequests(Model model) {
        model.addAttribute("cardRequests", cardService.getAllRequests()); //Загружаем все заявки
        return "moskvich_card_success";
    }


    @GetMapping("/moskvich_card/request")
    public String processMoskvichCardRequest(Principal principal, Model model) {
        String username = principal.getName();
        String requestNumber = cardService.getRequestNumberByUsername(username);

        System.out.println("🔍 Запрос на оформление карты москвича от: " + username);
        model.addAttribute("username", username);
        model.addAttribute("requestNumber", requestNumber);

        return "moskvich_card_request";
    }

    @GetMapping("/dashboard")
    public String showMfcDashboard(Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        System.out.println("🔍 Роль текущего пользователя: " + user.getRole());

        if (!user.getRole().equals(Role.MFC_EMPLOYEE)) {
            throw new AccessDeniedException("У вас нет прав для просмотра этой страницы.");
        }

        return "mfc_dashboard";
    }


    @GetMapping("/appointments/book")
    public String showBookAppointmentPage() {
        System.out.println("✅ Открытие формы записи на прием!"); // Проверка в консоли
        return "mfc_book_appointment"; //
    }


    @PostMapping("/appointments/update")
    public String updateAppointment(@RequestParam Long appointmentId,
                                    @RequestParam String patientName,
                                    @RequestParam LocalDate birthDate,
                                    @RequestParam String phoneNumber,
                                    @RequestParam String doctor,
                                    @RequestParam LocalDateTime date,
                                    @RequestParam String reason,
                                    @RequestParam String place,
                                    @RequestParam String status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Запись не найдена"));

        appointment.setPatientName(patientName);
        appointment.setBirthDate(birthDate);
        appointment.setPhoneNumber(phoneNumber);
        appointment.setDoctor(doctor);
        appointment.setDate(date);
        appointment.setReason(reason);
        appointment.setPlace(place);
        appointment.setStatus(status);

        appointmentRepository.save(appointment); // Сохраняем изменения в БД

        return "redirect:/mfc/appointments"; // Перенаправляем обратно на список записей
    }

    @GetMapping
    public String showDashboard(Principal principal, Model model) {
        model.addAttribute("username", principal.getName()); // Отправляем имя пользователя
        return "mfc_dashboard"; // Загружаем панель МФЦ
    }

    @GetMapping("/reports")
    public String showReportsPage() {
        return "development";
    }

    @GetMapping("/support")
    public String showSupportPage() {
        return "development";
    }

    @GetMapping("/settings")
    public String showSettingsPage() {
        return "development";

    }

    @PostMapping("/moskvich_card/update_status")
    public String updateCardStatus(@RequestParam Long requestId,
                                   @RequestParam String newStatus,
                                   RedirectAttributes redirectAttributes) {
        cardService.updateCardStatus(requestId, newStatus);
        return "redirect:/mfc/moskvich_card";
    }


}
