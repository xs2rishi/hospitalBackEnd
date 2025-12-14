package com.hospital.repository;

import com.hospital.entity.Prescription;
import com.hospital.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatient(User patient);
}
