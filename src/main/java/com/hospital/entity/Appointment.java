package com.hospital.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User patient;

    @ManyToOne
    private User doctor;

    private LocalDateTime appointmentTime;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; // BOOKED, COMPLETED, CANCELLED

    public enum AppointmentStatus {
        BOOKED, COMPLETED, CANCELLED
    }
}
