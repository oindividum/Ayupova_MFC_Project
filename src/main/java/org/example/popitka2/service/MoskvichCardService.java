package org.example.popitka2.service;


import jakarta.transaction.Transactional;
import org.example.popitka2.moskvich_card.MoskvichCardRequest;
import org.example.popitka2.repository.MoskvichCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

import java.util.List;

@Service
public class MoskvichCardService {
    private final MoskvichCardRepository moskvichCardRepository;

    @Autowired
    public MoskvichCardService(MoskvichCardRepository moskvichCardRepository) {
        this.moskvichCardRepository = moskvichCardRepository;
    }

    // Генерация уникального номера заявки
    private String generateRequestNumber() {
        return "MC-" + LocalDate.now().getYear() + "-" + (int)(Math.random() * 9000 + 1000);
    }

    // Создание новой заявки
    public void createRequest(MoskvichCardRequest request) {
        request.setRequestNumber(generateRequestNumber());
        request.setStatus("В ожидании");
        moskvichCardRepository.save(request); // Сохранение заявки вместе с пользователем
    }

    // Получение списка всех заявок
    public List<MoskvichCardRequest> getAllRequests() {
        return moskvichCardRepository.findAll();
    }

    // Поиск заявки по номеру
    public MoskvichCardRequest findByRequestNumber(String requestNumber) {
        List<MoskvichCardRequest> requests = moskvichCardRepository.findByRequestNumber(requestNumber);

        if (requests.isEmpty()) {
            throw new RuntimeException("❌ Ошибка: Заявка не найдена!");
        } else if (requests.size() > 1) {
            throw new RuntimeException("❌ Ошибка: Найдено несколько заявок с одним номером! Проверь данные!");
        }

        return requests.get(0); // Берём первую найденную заявку
    }

    // Обновление статуса заявки
    @Transactional
    public void updateCardStatus(Long requestId, String newStatus) {
        MoskvichCardRequest request = moskvichCardRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("❌ Ошибка: Заявка не найдена!"));

        request.setStatus(newStatus); // Обновляем статус заявки
        moskvichCardRepository.save(request);
    }

    // Получение статуса карты по имени пользователя
    public String getCardStatusByUsername(String username) {
        return moskvichCardRepository.findByUserUsername(username)
                .map(MoskvichCardRequest::getStatus)
                .orElse("❌ Карта не оформлена");
    }

    //  Получение номера заявки по имени пользователя
    public String getRequestNumberByUsername(String username) {
        String requestNumber = moskvichCardRepository.findRequestNumberByUsername(username);
        System.out.println("🔹 Найденный номер заявки для " + username + ": " + requestNumber);
        return requestNumber != null ? requestNumber : "❌ Нет активных заявок";
    }

    //  Поиск заявки по ID
    public MoskvichCardRequest findByRequestId(Long id) {
        return moskvichCardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Ошибка: Заявка с ID " + id + " не найдена!"));
    }
}
