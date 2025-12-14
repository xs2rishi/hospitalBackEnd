package com.hospital.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String fullName;
    private String email;
    private String phone;

    // For Doctors: Specialization, Fee
    private String specialization;
    private Double consultationFee;

    // For Patients: DOB, Gender
    private String dateOfBirth; // Storing as String for simplicity or LocalDate
    private String gender;
}
