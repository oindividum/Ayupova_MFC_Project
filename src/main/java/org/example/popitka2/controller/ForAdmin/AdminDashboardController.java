package org.example.popitka2.controller.ForAdmin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin/dashboard")
public class AdminDashboardController {

    @GetMapping
    public String viewAdminDashboard(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        return "admin_dashboard"; // Страница админ-панели
    }
    @GetMapping("/author")
    public String viewAuthorPage() {
        return "author"; // Страница "Об авторе"
    }
}

