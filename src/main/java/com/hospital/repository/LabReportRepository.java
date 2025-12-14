package com.hospital.repository;

import com.hospital.entity.LabReport;
import com.hospital.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LabReportRepository extends JpaRepository<LabReport, Long> {
    List<LabReport> findByPatient(User patient);
}
