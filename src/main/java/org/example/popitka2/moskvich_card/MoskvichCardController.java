package org.example.popitka2.moskvich_card;

import org.example.popitka2.model.Role;
import org.example.popitka2.model.User;
import org.example.popitka2.repository.UserRepository;
import org.example.popitka2.service.MoskvichCardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/moskvich_card")
public class MoskvichCardController {
    private final MoskvichCardService moskvichCardService;
    private final UserRepository userRepository;

    public MoskvichCardController(MoskvichCardService moskvichCardService, UserRepository userRepository) {
        this.moskvichCardService = moskvichCardService;
        this.userRepository = userRepository;
    }

    //Главная страница карты москвича
    @GetMapping("")
    public String showMainPage(Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        String requestNumber = moskvichCardService.getRequestNumberByUsername(user.getUsername());

        //  Если заявки нет, передаем "Нет активных заявок"
        if (requestNumber == null || requestNumber.isEmpty()) {
            requestNumber = "❌ Нет активных заявок";
        }

        model.addAttribute("username", user.getUsername());
        model.addAttribute("requestNumber", requestNumber);

        return "moskvich_card";
    }

    // Страница редактирования заявки
    @GetMapping("/edit/{id}")
    public String editCard(@PathVariable Long id, Model model) {
        MoskvichCardRequest card = moskvichCardService.findByRequestId(id);
        model.addAttribute("card", card);
        return "moskvich_card_edit";
    }

    //  Страница подачи заявки
    @GetMapping("/request")
    public String showRequestForm(Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        String requestNumber = moskvichCardService.getRequestNumberByUsername(user.getUsername());

        model.addAttribute("username", user.getUsername());
        model.addAttribute("requestNumber", requestNumber);
        model.addAttribute("request", new MoskvichCardRequest());

        return "moskvich_card_request";
    }

    // Обработка подачи заявки
    @PostMapping("/request")
    public String submitRequest(@ModelAttribute MoskvichCardRequest request, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        request.setUser(user); // Привязываем заявку к пользователю
        moskvichCardService.createRequest(request);

        // Проверяем роль перед редиректом
        if (user.getRole() == Role.MFC_EMPLOYEE) {
            return "redirect:/mfc/moskvich_card/success"; //  Если сотрудник МФЦ, ведем на успешную страницу!
        } else {
            return "redirect:/moskvich_card/status"; //  Если обычный пользователь, ведем на статус заявки!
        }
    }


    @GetMapping("/status")
    public String showStatusPage(Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        String requestNumber = moskvichCardService.getRequestNumberByUsername(user.getUsername());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("requestNumber", requestNumber);

        if (requestNumber != null) {
            MoskvichCardRequest request = moskvichCardService.findByRequestNumber(requestNumber);
            model.addAttribute("request", request); // Передаём заявку в модель
            model.addAttribute("cardStatus", request.getStatus()); // Передаём статус карты
        }

        return "moskvich_card_status";
    }



    // Список всех заявок
    @GetMapping("/list")
    public String showAllRequests(Model model) {
        List<MoskvichCardRequest> cardRequests = moskvichCardService.getAllRequests();
        model.addAttribute("cardRequests", cardRequests);
        return "moskvich_card_success";
    }
}
