package org.example.popitka2.controller.ForAdmin;
import org.example.popitka2.repository.MoskvichCardRepository;
import org.example.popitka2.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.example.popitka2.model.Role;

import org.example.popitka2.model.User;
import org.example.popitka2.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MoskvichCardRepository moskvichCardRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin_users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/delete_user/{id}")
    public String deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        moskvichCardRepository.deleteAll(user.getMoskvichCardRequests());
        userRepository.delete(user);
        return "redirect:/admin/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        model.addAttribute("user", user);
        return "edit_user_role";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/edit/{id}")
    public String updateUserRole(@PathVariable Long id,
                                 @RequestParam("role") Role newRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        user.setRole(newRole);
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/development")
    public String showDevelopmentPage() {
        return "development"; // templates/development.html
    }
}
