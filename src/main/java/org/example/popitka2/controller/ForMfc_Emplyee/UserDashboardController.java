package org.example.popitka2.controller.ForMfc_Emplyee;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;


@Controller
@RequestMapping("/user/dashboard")

public class UserDashboardController {

    @GetMapping
    public String showDashboard(Principal principal, Model model) {
        model.addAttribute("username", principal.getName()); // Отправляем имя пользователя
        return "user_dashboard"; // Загружаем личный кабинет
    }
}