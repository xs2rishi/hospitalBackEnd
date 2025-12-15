package com.hospital.repository;

import com.hospital.entity.Appointment;
import com.hospital.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctor(User doctor);

    List<Appointment> findByPatient(User patient);

    @org.springframework.data.jpa.repository.Query("SELECT new map(CAST(a.appointmentTime AS date) as date, COUNT(DISTINCT a.doctor) as count) FROM Appointment a GROUP BY CAST(a.appointmentTime AS date)")
    List<java.util.Map<String, Object>> countDoctorsWithAppointmentsPerDay();

    long countByStatusAndAppointmentTimeBetween(com.hospital.entity.Appointment.AppointmentStatus status,
            java.time.LocalDateTime start, java.time.LocalDateTime end);

    long countDistinctDoctorByStatusAndAppointmentTimeBetween(com.hospital.entity.Appointment.AppointmentStatus status,
            java.time.LocalDateTime start, java.time.LocalDateTime end);

    @org.springframework.data.jpa.repository.Query("SELECT new map(a.doctor.fullName as doctorName, COUNT(DISTINCT CAST(a.appointmentTime AS date)) as daysWorked, COUNT(a) as patientsSeen) FROM Appointment a WHERE a.status = :status AND a.appointmentTime BETWEEN :start AND :end GROUP BY a.doctor")
    List<java.util.Map<String, Object>> getMonthlyDetailedStats(
            @org.springframework.data.repository.query.Param("status") com.hospital.entity.Appointment.AppointmentStatus status,
            @org.springframework.data.repository.query.Param("start") java.time.LocalDateTime start,
            @org.springframework.data.repository.query.Param("end") java.time.LocalDateTime end);
}
