package com.example.medicalsystem.controller;

import com.example.medicalsystem.dto.DoctorPatientRelationDTO;
import com.example.medicalsystem.exception.NotFoundException;
import com.example.medicalsystem.service.DoctorPatientRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/relations")
@CrossOrigin(origins = "*")
public class DoctorPatientRelationController {

    private final DoctorPatientRelationService relationService;

    @Autowired
    public DoctorPatientRelationController(DoctorPatientRelationService relationService) {
        this.relationService = relationService;
    }

    @GetMapping
    public ResponseEntity<List<DoctorPatientRelationDTO>> getAllRelations() {
        List<DoctorPatientRelationDTO> relations = relationService.getAllRelations();
        return ResponseEntity.ok(relations);
    }

    @PostMapping
    public ResponseEntity<?> addRelation(@RequestParam Long doctorId, @RequestParam Long patientId) {
        try {
            DoctorPatientRelationDTO relation = relationService.addRelation(doctorId, patientId);
            return ResponseEntity.status(HttpStatus.CREATED).body(relation);
        } catch (NotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/{relationId}")
    public ResponseEntity<?> updateRelation(
            @PathVariable Long relationId,
            @RequestParam Long doctorId,
            @RequestParam Long patientId) {
        try {
            DoctorPatientRelationDTO relation = relationService.updateRelation(relationId, doctorId, patientId);
            return ResponseEntity.ok(relation);
        } catch (NotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{relationId}")
    public ResponseEntity<?> deleteRelation(@PathVariable Long relationId) {
        try {
            relationService.deleteRelation(relationId);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
