package com.hospital.controller;

import com.hospital.entity.*;
import com.hospital.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/clinical")
@CrossOrigin(origins = "*")
public class ClinicalController {

    @Autowired
    private VitalsRepository vitalsRepository;
    @Autowired
    private LabReportRepository labReportRepository;
    @Autowired
    private UserRepository userRepository;

    private final Path rootLocation = Paths.get("uploads");

    @PostMapping("/vitals")
    public Vitals recordVitals(@RequestBody Vitals vitals) {
        return vitalsRepository.save(vitals);
    }

    @PostMapping("/labs/upload")
    public LabReport uploadLabReport(@RequestParam("patientId") Long patientId,
            @RequestParam("testName") String testName,
            @RequestParam("file") MultipartFile file) throws IOException {

        if (Files.notExists(rootLocation)) {
            Files.createDirectories(rootLocation);
        }

        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), rootLocation.resolve(filename));

        LabReport report = new LabReport();
        report.setPatient(userRepository.findById(patientId).orElseThrow());
        report.setTestName(testName);
        report.setFileUrl(rootLocation.resolve(filename).toString());
        report.setUploadedAt(java.time.LocalDateTime.now());

        return labReportRepository.save(report);
    }
}
