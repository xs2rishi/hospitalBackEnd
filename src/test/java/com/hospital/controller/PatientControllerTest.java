package com.hospital.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.entity.*;
import com.hospital.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
@SuppressWarnings("null")
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AppointmentRepository appointmentRepository;
    @MockBean
    private VisitHistoryRepository visitHistoryRepository;
    @MockBean
    private PrescriptionRepository prescriptionRepository;
    @MockBean
    private LabReportRepository labReportRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetDoctors() throws Exception {
        when(userRepository.findByRole(Role.DOCTOR)).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/patients/doctors"))
                .andExpect(status().isOk());
    }

    @Test
    public void testBookAppointment() throws Exception {
        Appointment appointment = new Appointment();
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        mockMvc.perform(post("/api/patients/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointment)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetMyAppointments() throws Exception {
        User patient = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findByPatient(patient)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/patients/1/appointments"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetMyAppointmentsNotFound() throws Exception {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/patients/99/appointments"))
                .andExpect(status().isInternalServerError()); // GlobalExceptionHandler catches NoSuchElementException
    }
}
