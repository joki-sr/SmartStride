package com.example.medicalsystem.controller;

import com.example.medicalsystem.dto.PatientReportDTO;
import com.example.medicalsystem.exception.NotFoundException;
import com.example.medicalsystem.service.PatientReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class PatientReportController {

    private final PatientReportService reportService;

    @Autowired
    public PatientReportController(PatientReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PatientReportDTO>> getReportsByPatientId(@PathVariable Long patientId) {
        List<PatientReportDTO> reports = reportService.getReportsByPatientId(patientId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/patient/{patientId}/type/{type}")
    public ResponseEntity<List<PatientReportDTO>> getReportsByPatientIdAndType(
            @PathVariable Long patientId,
            @PathVariable String type) {
        List<PatientReportDTO> reports = reportService.getReportsByPatientIdAndType(patientId, type);
        return ResponseEntity.ok(reports);
    }

    @PostMapping("/patient/{patientId}")
    public ResponseEntity<?> createReport(
            @PathVariable Long patientId,
            @RequestBody PatientReportDTO reportDTO) {
        try {
            PatientReportDTO report = reportService.createReport(patientId, reportDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(report);
        } catch (NotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<?> deleteReport(@PathVariable Long reportId) {
        try {
            reportService.deleteReport(reportId);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
