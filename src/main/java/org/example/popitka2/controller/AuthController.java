package org.example.popitka2.controller;

import org.example.popitka2.model.Role;
import org.example.popitka2.model.User;
import org.example.popitka2.repository.UserRepository;
import org.example.popitka2.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private static final String MFC_SECRET_CODE = "MFC12345"; // Код сотрудника МФЦ

    public AuthController(UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

@PostMapping("/register")
public String registerUser(@ModelAttribute User user,
                           @RequestParam(required = false) String mfcCode,
                           Model model) {
    if (userRepository.existsByUsername(user.getUsername())) {
        model.addAttribute("error", "Пользователь уже существует");
        return "register";
    }

    // Определяем роль пользователя на основе введенного кода
    user.setRole(mfcCode != null && mfcCode.equals(MFC_SECRET_CODE) ? Role.MFC_EMPLOYEE : Role.USER);

    // Логируем роль перед сохранением
    System.out.println("Назначенная роль: " + user.getRole());


    // Если `birthDate` не введено, оставляем `null`
    if (user.getBirthDate() == null) {
        user.setBirthDate(null);
    }

    if (user.getPhoneNumber() == null) {
        user.setPhoneNumber("Не указан"); // Теперь можно зарегистрироваться без номера
    }
    if (user.getFullName() == null || user.getFullName().isEmpty()) {
        user.setFullName("Не указано");
    }
    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
        throw new RuntimeException("❌ Ошибка: Пользователь с таким именем уже существует!");
    }


    // Хешируем пароль перед сохранением
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);

    // Проверяем роль, получая пользователя из БД
    User savedUser = userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new RuntimeException("Ошибка при сохранении"));
    System.out.println("Роль сохраненного пользователя: " + savedUser.getRole());

    return "redirect:/auth/login";
}

    @GetMapping("/auth/login")
    public String showLoginPage() {
        return "login";
    }
}
