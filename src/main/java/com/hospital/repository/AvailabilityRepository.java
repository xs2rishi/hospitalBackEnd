package com.hospital.repository;

import com.hospital.entity.Availability;
import com.hospital.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByDoctor(User doctor);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(DISTINCT a.doctor) FROM Availability a WHERE a.dayOfWeek = :dayOfWeek")
    long countDoctorsAvailable(@org.springframework.web.bind.annotation.RequestParam("dayOfWeek") String dayOfWeek);
}
