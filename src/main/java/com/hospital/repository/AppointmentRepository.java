package com.hospital.repository;

import com.hospital.entity.Appointment;
import com.hospital.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctor(User doctor);

    List<Appointment> findByPatient(User patient);
}
