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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StaffController.class)
@SuppressWarnings("null")
public class StaffControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AvailabilityRepository availabilityRepository;
    @MockBean
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSetAvailability() throws Exception {
        User doctor = new User();
        Availability availability = new Availability();

        when(userRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(availabilityRepository.save(any(Availability.class))).thenReturn(availability);

        mockMvc.perform(post("/api/staff/doctors/1/availability")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(availability)))
                .andExpect(status().isOk());
    }

    @Test
    public void testSetAvailabilityDoctorNotFound() throws Exception {
        Availability availability = new Availability();
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/staff/doctors/99/availability")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(availability)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testUpdateStatus() throws Exception {
        Appointment appointment = new Appointment();
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        mockMvc.perform(put("/api/staff/appointments/1/status")
                .param("status", "COMPLETED"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetDailySummary() throws Exception {
        when(appointmentRepository.countByStatusAndAppointmentTimeBetween(any(), any(), any())).thenReturn(10L);
        when(availabilityRepository.countDoctorsAvailable(anyString())).thenReturn(5L);

        mockMvc.perform(get("/api/staff/stats/summary")
                .param("date", "2023-10-27"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPatientsVisited").value(10))
                .andExpect(jsonPath("$.doctorsAvailable").value(5));
    }

    @Test
    public void testMonthlyDistinctDoctorReport() throws Exception {
        when(appointmentRepository.countByStatusAndAppointmentTimeBetween(any(), any(), any())).thenReturn(50L);
        when(appointmentRepository.countDistinctDoctorByStatusAndAppointmentTimeBetween(any(), any(), any()))
                .thenReturn(8L);

        mockMvc.perform(get("/api/staff/stats/monthly-summary")
                .param("year", "2023")
                .param("month", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPatientsVisited").value(50))
                .andExpect(jsonPath("$.totalDoctorsVisited").value(8));
    }

    @Test
    public void testMonthlyDetailedBillingReport() throws Exception {
        java.util.List<java.util.Map<String, Object>> mockStats = java.util.Collections.singletonList(
                java.util.Map.of("doctorName", "Dr. Smith", "daysWorked", 20, "patientsSeen", 100));
        when(appointmentRepository.getMonthlyDetailedStats(any(), any(), any())).thenReturn(mockStats);

        mockMvc.perform(get("/api/staff/stats/monthly-detailed")
                .param("year", "2023")
                .param("month", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctorName").value("Dr. Smith"))
                .andExpect(jsonPath("$[0].daysWorked").value(20))
                .andExpect(jsonPath("$[0].patientsSeen").value(100));
    }
}
