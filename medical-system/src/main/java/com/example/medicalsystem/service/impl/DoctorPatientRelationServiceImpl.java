package com.example.medicalsystem.service.impl;

import com.example.medicalsystem.dto.DoctorPatientRelationDTO;
import com.example.medicalsystem.entity.Doctor;
import com.example.medicalsystem.entity.DoctorPatientRelation;
import com.example.medicalsystem.entity.Patient;
import com.example.medicalsystem.exception.NotFoundException;
import com.example.medicalsystem.repository.DoctorPatientRelationRepository;
import com.example.medicalsystem.repository.DoctorRepository;
import com.example.medicalsystem.repository.PatientRepository;
import com.example.medicalsystem.service.DoctorPatientRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorPatientRelationServiceImpl implements DoctorPatientRelationService {

    private final DoctorPatientRelationRepository relationRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public DoctorPatientRelationServiceImpl(
            DoctorPatientRelationRepository relationRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository) {
        this.relationRepository = relationRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public List<DoctorPatientRelationDTO> getAllRelations() {
        return relationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DoctorPatientRelationDTO addRelation(Long doctorId, Long patientId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new NotFoundException("医生不存在"));
        
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new NotFoundException("患者不存在"));
        
        DoctorPatientRelation relation = new DoctorPatientRelation();
        relation.setDoctor(doctor);
        relation.setPatient(patient);
        
        DoctorPatientRelation savedRelation = relationRepository.save(relation);
        
        return convertToDTO(savedRelation);
    }

    @Override
    @Transactional
    public DoctorPatientRelationDTO updateRelation(Long relationId, Long doctorId, Long patientId) throws NotFoundException {
        DoctorPatientRelation relation = relationRepository.findById(relationId)
                .orElseThrow(() -> new NotFoundException("关系不存在"));
        
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new NotFoundException("医生不存在"));
        
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new NotFoundException("患者不存在"));
        
        relation.setDoctor(doctor);
        relation.setPatient(patient);
        
        DoctorPatientRelation updatedRelation = relationRepository.save(relation);
        
        return convertToDTO(updatedRelation);
    }

    @Override
    @Transactional
    public void deleteRelation(Long relationId) throws NotFoundException {
        if (!relationRepository.existsById(relationId)) {
            throw new NotFoundException("关系不存在");
        }
        
        relationRepository.deleteById(relationId);
    }
    
    private DoctorPatientRelationDTO convertToDTO(DoctorPatientRelation relation) {
        DoctorPatientRelationDTO dto = new DoctorPatientRelationDTO();
        dto.setId(relation.getId());
        dto.setDoctorId(relation.getDoctor().getId());
        dto.setDoctorName(relation.getDoctor().getUser().getName());
        dto.setPatientId(relation.getPatient().getId());
        dto.setPatientName(relation.getPatient().getUser().getName());
        return dto;
    }
}
