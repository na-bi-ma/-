package org.example.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "VRACH_ID", nullable = false)
    private Long vrachId;

    @Column(name = "USLUGA_ID")
    private Long uslugaId;

    @Column(name = "APPOINTMENT_DATE", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "APPOINTMENT_TIME", nullable = false)
    private LocalTime appointmentTime;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Appointment() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getVrachId() { return vrachId; }
    public void setVrachId(Long vrachId) { this.vrachId = vrachId; }

    public Long getUslugaId() { return uslugaId; }
    public void setUslugaId(Long uslugaId) { this.uslugaId = uslugaId; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    public LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}