package com.example.medicalsystem.service.impl;

import com.example.medicalsystem.dto.PatientDTO;
import com.example.medicalsystem.entity.Patient;
import com.example.medicalsystem.repository.PatientRepository;
import com.example.medicalsystem.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PatientDTO convertToDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setName(patient.getUser().getName());
        dto.setPhone(patient.getUser().getPhone());
        dto.setGender(patient.getUser().getGender());
        dto.setBirthdate(patient.getUser().getBirthDate());
        dto.setIdCard(patient.getUser().getIdCard());
        dto.setPassport(patient.getUser().getPassport());
        return dto;
    }
}
