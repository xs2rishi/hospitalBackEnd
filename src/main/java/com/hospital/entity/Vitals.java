package com.hospital.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Vitals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User patient;

    @ManyToOne
    private User nurse; // Optional, who recorded it

    private LocalDateTime recordedAt;

    private Double height;
    private Double weight;
    private String bloodPressure;
    private Double temperature;
}
