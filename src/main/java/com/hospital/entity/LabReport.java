package com.hospital.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class LabReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User patient;

    private String testName;
    private String fileUrl; // Local path or URL
    private LocalDateTime uploadedAt;

    @ManyToOne
    private User uploadedBy; // Patient or Nurse/Staff
}
