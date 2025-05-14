package org.example.popitka2.controller.ForAdmin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.popitka2.service.AnalyticsService;

import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;


@Controller
@RequestMapping("/admin/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @Autowired
    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("")
    public String showAnalytics(Model model) throws JsonProcessingException {
        //  Получаем данные из `AnalyticsService`
        long totalUsers = analyticsService.getTotalUsers(); // Количество пользователей
        Double avgProcessingTime = analyticsService.getAvgProcessingTime(); // Среднее время обработки
        Map<String, Integer> stats = analyticsService.getAppointmentStats(); // Статистика заявок
        String statsJson = new ObjectMapper().writeValueAsString(stats); // Конвертируем в JSON

        //  Передаём данные в модель
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("avgProcessingTime", avgProcessingTime != null ? avgProcessingTime : 0);
        model.addAttribute("activeRequests", stats.getOrDefault("Записан", 0)); // Активные заявки
        model.addAttribute("completedRequests", stats.getOrDefault("Завершено", 0)); // Завершённые заявки
        model.addAttribute("appointmentStatsJson", statsJson); // JSON для графика

        return "admin_analytics"; // Загружаем страницу `admin_analytics.html`
    }
}
