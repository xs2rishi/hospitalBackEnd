package com.hospital.controller;

import com.hospital.entity.*;
import com.hospital.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private VisitHistoryRepository visitHistoryRepository;
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private LabReportRepository labReportRepository;

    @GetMapping("/doctors")
    public List<User> getAllDoctors() {
        return userRepository.findByRole(Role.DOCTOR);
    }

    @PostMapping("/appointments")
    public Appointment bookAppointment(@RequestBody Appointment appointment) {
        appointment.setStatus(Appointment.AppointmentStatus.BOOKED);
        return appointmentRepository.save(appointment);
    }

    @GetMapping("/{id}/appointments")
    public List<Appointment> getMyAppointments(@PathVariable Long id) {
        User patient = userRepository.findById(id).orElseThrow();
        return appointmentRepository.findByPatient(patient);
    }

    @GetMapping("/{id}/history")
    public List<VisitHistory> getHistory(@PathVariable Long id) {
        User patient = userRepository.findById(id).orElseThrow();
        return visitHistoryRepository.findByPatient(patient);
    }

    @GetMapping("/{id}/prescriptions")
    public List<Prescription> getPrescriptions(@PathVariable Long id) {
        User patient = userRepository.findById(id).orElseThrow();
        return prescriptionRepository.findByPatient(patient);
    }

    @GetMapping("/{id}/labs")
    public List<LabReport> getLabReports(@PathVariable Long id) {
        User patient = userRepository.findById(id).orElseThrow();
        return labReportRepository.findByPatient(patient);
    }
}
