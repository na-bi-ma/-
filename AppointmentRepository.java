package org.example.repository;

import org.example.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByUserIdOrderByAppointmentDateDesc(Long userId);
    List<Appointment> findByVrachIdAndAppointmentDate(Long vrachId, java.time.LocalDate date);
}