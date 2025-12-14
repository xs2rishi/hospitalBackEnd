package com.hospital.controller;

import com.hospital.entity.*;
import com.hospital.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "*")
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
}
