package org.example.popitka2.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String patientName;

    @Column(nullable = false)
    private String doctor;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String status; // Статус приема: "Запланировано", "Завершено" и т.д.

    private LocalDate birthDate;
    private String place; //
    private String phoneNumber;
    private String reason;     // Пустой конструктор для JPA
    public Appointment() { }

    // Основной конструктор
    public Appointment(String patientName, LocalDate birthDate, String phoneNumber, String doctor,
                       LocalDateTime appointmentDate, String status, String reason, String place) {
        this.patientName = patientName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.doctor = doctor;
        this.date = appointmentDate;
        this.status = status;
        this.reason = reason;
        this.place = place;
    }



    // Геттеры и сеттеры
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public Long getId() { return id; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public String getDoctor() { return doctor; }
    public void setDoctor(String doctor) { this.doctor = doctor; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
