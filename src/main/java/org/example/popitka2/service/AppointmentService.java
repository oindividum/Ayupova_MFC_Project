package org.example.popitka2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.popitka2.repository.AppointmentRepository;
import org.example.popitka2.model.Appointment;
import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.example.popitka2.model.User;


@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    //Получить все записи на прием
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll(); // Загружаем все записи из БД
    }

    //Записать пользователя на прием
    public void bookAppointment(String patientName, LocalDate birthDate, String phoneNumber, String doctor,
                                LocalDateTime appointmentDate, String reason, String place) {
        Appointment appointment = new Appointment(patientName, birthDate, phoneNumber, doctor, appointmentDate, "Записан", reason, place);

        System.out.println("✅ Запись перед сохранением: " + appointment);

        appointmentRepository.save(appointment);

        System.out.println("✅ Запись успешно сохранена в БД!");
    }

    public List<Appointment> getAppointmentsByUser(User user) {
        return appointmentRepository.findByPatientName(user.getUsername());
    }


    // Отметить прием как завершенный
    public void completeAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Запись не найдена"));
        appointment.setStatus("Завершено");
        appointmentRepository.save(appointment);
    }
    public void updateAppointment(Long id, String patientName, String doctor, LocalDateTime date, String place) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Запись не найдена!"));

        appointment.setPatientName(patientName);
        appointment.setDoctor(doctor);
        appointment.setDate(date);
        appointment.setPlace(place);

        appointmentRepository.save(appointment);
    }

}
