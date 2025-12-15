package com.hospital.controller;

import com.hospital.entity.*;
import com.hospital.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "*")
@SuppressWarnings("null")
public class StaffController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AvailabilityRepository availabilityRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping("/doctors")
    public List<User> getAllDoctors() {
        return userRepository.findByRole(Role.DOCTOR);
    }

    @PostMapping("/doctors/{id}/availability")
    public Availability setAvailability(@PathVariable Long id, @RequestBody Availability availability) {
        User doctor = userRepository.findById(id).orElseThrow();
        availability.setDoctor(doctor);
        return availabilityRepository.save(availability);
    }

    @GetMapping("/appointments")
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @PutMapping("/appointments/{id}/status")
    public Appointment updateStatus(@PathVariable Long id, @RequestParam Appointment.AppointmentStatus status) {
        Appointment apt = appointmentRepository.findById(id).orElseThrow();
        apt.setStatus(status);
        return appointmentRepository.save(apt);
    }

    @GetMapping("/availabilities")
    public List<Availability> getAllAvailabilities() {
        return availabilityRepository.findAll();
    }

    @GetMapping("/doctors/{id}/availability")
    public List<Availability> getDoctorAvailability(@PathVariable Long id) {
        User doctor = userRepository.findById(id).orElseThrow();
        return availabilityRepository.findByDoctor(doctor);
    }

    @GetMapping("/stats/daily-appointments")
    public List<java.util.Map<String, Object>> getDailyAppointmentStats() {
        return appointmentRepository.countDoctorsWithAppointmentsPerDay();
    }

    @GetMapping("/stats/summary")
    public java.util.Map<String, Object> getDailySummary(@RequestParam String date) { // date format YYYY-MM-DD
        java.time.LocalDate localDate = java.time.LocalDate.parse(date);
        java.time.LocalDateTime startOfDay = localDate.atStartOfDay();
        java.time.LocalDateTime endOfDay = localDate.atTime(java.time.LocalTime.MAX);
        String dayOfWeek = localDate.getDayOfWeek().name();

        long patientsVisited = appointmentRepository.countByStatusAndAppointmentTimeBetween(
                Appointment.AppointmentStatus.COMPLETED, startOfDay, endOfDay);

        long doctorsAvailable = availabilityRepository.countDoctorsAvailable(dayOfWeek);

        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("totalPatientsVisited", patientsVisited);
        response.put("doctorsAvailable", doctorsAvailable);
        return response;
    }

    @GetMapping("/stats/monthly-summary")
    public java.util.Map<String, Object> getMonthlyDistinctDoctorReport(@RequestParam int year,
            @RequestParam int month) {
        java.time.YearMonth yearMonth = java.time.YearMonth.of(year, month);
        java.time.LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        java.time.LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(java.time.LocalTime.MAX);

        long patientsVisited = appointmentRepository.countByStatusAndAppointmentTimeBetween(
                Appointment.AppointmentStatus.COMPLETED, startOfMonth, endOfMonth);

        long doctorsVisited = appointmentRepository.countDistinctDoctorByStatusAndAppointmentTimeBetween(
                Appointment.AppointmentStatus.COMPLETED, startOfMonth, endOfMonth);

        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("totalPatientsVisited", patientsVisited);
        response.put("totalDoctorsVisited", doctorsVisited);
        return response;
    }

    @GetMapping("/stats/monthly-detailed")
    public List<java.util.Map<String, Object>> getMonthlyDetailedBillingReport(@RequestParam int year,
            @RequestParam int month) {
        java.time.YearMonth yearMonth = java.time.YearMonth.of(year, month);
        java.time.LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        java.time.LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(java.time.LocalTime.MAX);

        return appointmentRepository.getMonthlyDetailedStats(
                Appointment.AppointmentStatus.COMPLETED, startOfMonth, endOfMonth);
    }
}
