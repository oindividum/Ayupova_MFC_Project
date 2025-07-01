package org.example.popitka2.service;


import jakarta.transaction.Transactional;
import org.example.popitka2.model.Role;
import org.example.popitka2.model.User;
import org.example.popitka2.repository.MoskvichCardRepository;
import org.example.popitka2.moskvich_card.MoskvichCardRequest;
import org.example.popitka2.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MoskvichCardRepository moskvichCardRepository;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MoskvichCardRepository moskvichCardRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.moskvichCardRepository = moskvichCardRepository;
    }
    @Transactional

    public User registerUser(String username, String fullName, String email, String password, String mfcCode) {
        Role role = Role.USER;

        if (mfcCode != null && mfcCode.equals("SPECIAL_MFC_CODE")) {
            role = Role.MFC_EMPLOYEE;
        }

        User user = new User(username, fullName, email, passwordEncoder.encode(password), role);

        //  Устанавливаем значения по умолчанию, если они не вводятся
        user.setPhoneNumber("Не указан");
        user.setBirthDate(LocalDate.of(2000, 1, 1)); //  Дата рождения по умолчанию
        if (fullName == null || fullName.isEmpty()) {
            throw new RuntimeException("❌ Ошибка: Имя пользователя не может быть пустым!");
        }
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Пользователь с таким именем уже существует!");
        }

        return userRepository.save(user);
    }



    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void updateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("Пользователь не найден: " + user.getId())
        );
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());
        userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateCardStatus(Long requestId, String newStatus) {
        MoskvichCardRequest request = moskvichCardRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("❌ Ошибка: Заявка не найдена!"));

        request.setStatus(newStatus);
        moskvichCardRepository.save(request);
    }

}
