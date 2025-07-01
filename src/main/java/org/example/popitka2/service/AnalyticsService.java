package org.example.popitka2.service;

import org.example.popitka2.repository.AppointmentRepository;
import org.example.popitka2.repository.UserRepository;
import org.example.popitka2.repository.MoskvichCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final AppointmentRepository appointmentRepository;

    public AnalyticsService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MoskvichCardRepository moskvichCardRepository;

    // Получаем общее количество пользователей
    public long getTotalUsers() {
        return userRepository.countUsers();
    }

    //  Получаем среднее время обработки заявок
    public Double getAvgProcessingTime() {
        return moskvichCardRepository.getAverageProcessingTime();
    }

    public Map<String, Integer> getAppointmentStats() {
        Map<String, Long> originalStats = new HashMap<>();
        originalStats.put("Записан", appointmentRepository.countByStatus("Записан"));
        originalStats.put("✅ Записан", appointmentRepository.countByStatus("✅ Записан"));
        originalStats.put("❗ Ожидание подтверждения", appointmentRepository.countByStatus("❗ Ожидание подтверждения"));
        originalStats.put("Завершено", appointmentRepository.countByStatus("Завершено"));
        originalStats.put("❌ Отменено", appointmentRepository.countByStatus("❌ Отменено"));

        // Преобразуем Long в Integer
        return originalStats.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().intValue()));
    }

    public void printAvailableStatuses() {
        List<String> statuses = appointmentRepository.findAllStatuses();
        System.out.println("📊 Доступные статусы в БД: " + statuses);
    }


}
