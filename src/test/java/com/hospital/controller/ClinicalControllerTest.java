package com.hospital.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.entity.*;
import com.hospital.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClinicalController.class)
@SuppressWarnings("null")
public class ClinicalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VitalsRepository vitalsRepository;
    @MockBean
    private LabReportRepository labReportRepository;
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRecordVitals() throws Exception {
        Vitals vitals = new Vitals();
        when(vitalsRepository.save(any(Vitals.class))).thenReturn(vitals);

        mockMvc.perform(post("/api/clinical/vitals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vitals)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUploadLabReport() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "some data".getBytes());
        User patient = new User();

        when(userRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(labReportRepository.save(any(LabReport.class))).thenReturn(new LabReport());

        mockMvc.perform(multipart("/api/clinical/labs/upload")
                .file(file)
                .param("patientId", "1")
                .param("testName", "Blood Test"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUploadLabReportPatientNotFound() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "some data".getBytes());
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(multipart("/api/clinical/labs/upload")
                .file(file)
                .param("patientId", "99")
                .param("testName", "Blood Test"))
                .andExpect(status().isInternalServerError());
    }
}
