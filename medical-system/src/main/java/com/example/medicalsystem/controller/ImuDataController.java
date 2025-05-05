package com.example.medicalsystem.controller;

import com.example.medicalsystem.dto.UploadResultDTO;
import com.example.medicalsystem.exception.NotFoundException;
import com.example.medicalsystem.service.ImuDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/imu")
@CrossOrigin(origins = "*")
public class ImuDataController {

    private final ImuDataService imuDataService;

    @Autowired
    public ImuDataController(ImuDataService imuDataService) {
        this.imuDataService = imuDataService;
    }

    @PostMapping("/patient/{patientId}/upload")
    public ResponseEntity<?> uploadData(
            @PathVariable Long patientId,
            @RequestBody List<Map<String, Object>> dataPoints) {
        try {
            UploadResultDTO result = imuDataService.uploadData(patientId, dataPoints);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (NotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Map<String, Object>>> getDataByPatientId(
            @PathVariable Long patientId,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        List<Map<String, Object>> data = imuDataService.getDataByPatientId(patientId, startTime, endTime);
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/patient/{patientId}")
    public ResponseEntity<?> deleteDataByPatientId(
            @PathVariable Long patientId,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        imuDataService.deleteDataByPatientId(patientId, startTime, endTime);
        return ResponseEntity.noContent().build();
    }
}
