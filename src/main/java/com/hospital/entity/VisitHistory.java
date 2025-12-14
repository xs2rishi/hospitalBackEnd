package com.hospital.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class VisitHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User patient;

    @ManyToOne
    private User doctor;

    private LocalDateTime visitDate;

    @Column(length = 2000)
    private String diagnosis;

    @Column(length = 2000)
    private String notes;
}
