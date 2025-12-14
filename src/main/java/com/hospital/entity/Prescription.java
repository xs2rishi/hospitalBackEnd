package com.hospital.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User patient;

    @ManyToOne
    private User doctor;

    private LocalDateTime prescribedAt;

    @Column(length = 2000)
    private String medicationDetails; // E.g. "Paracetamol 500mg - 2x daily"

    private String instructions;
}
