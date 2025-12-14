package com.hospital.repository;

import com.hospital.entity.Vitals;
import com.hospital.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VitalsRepository extends JpaRepository<Vitals, Long> {
    List<Vitals> findByPatient(User patient);
}
