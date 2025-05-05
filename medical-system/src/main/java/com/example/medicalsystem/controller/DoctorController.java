package com.example.medicalsystem.controller;

import com.example.medicalsystem.dto.DoctorDTO;
import com.example.medicalsystem.dto.DoctorRegistrationDTO;
import com.example.medicalsystem.exception.ConflictException;
import com.example.medicalsystem.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<DoctorDTO> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerDoctor(@Valid @RequestBody DoctorRegistrationDTO doctorDTO) {
        try {
            DoctorDTO doctor = doctorService.registerDoctor(doctorDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(doctor);
        } catch (ConflictException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
}
