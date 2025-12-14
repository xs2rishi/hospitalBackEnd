package com.hospital.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalTime;

@Entity
@Data
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User doctor;

    private String dayOfWeek; // MONDAY, TUESDAY...
    private LocalTime startTime;
    private LocalTime endTime;
}
