package com.hospital.repository;

import com.hospital.entity.VisitHistory;
import com.hospital.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VisitHistoryRepository extends JpaRepository<VisitHistory, Long> {
    List<VisitHistory> findByPatient(User patient);
}
