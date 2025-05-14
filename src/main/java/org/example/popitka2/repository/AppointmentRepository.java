package org.example.popitka2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.popitka2.model.Appointment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;



public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAll();
    Optional<Appointment> findById(Long id);
    List<Appointment> findByPatientName(String patientName);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.status = :status")
    Long countByStatus(@Param("status") String status);
    @Query("SELECT DISTINCT a.status FROM Appointment a")
    List<String> findAllStatuses();


}
