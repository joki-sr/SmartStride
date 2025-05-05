package com.example.medicalsystem.repository;

import com.example.medicalsystem.entity.DoctorPatientRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorPatientRelationRepository extends JpaRepository<DoctorPatientRelation, Long> {
    List<DoctorPatientRelation> findAll();
    List<DoctorPatientRelation> findByDoctorId(Long doctorId);
    Optional<DoctorPatientRelation> findByDoctorIdAndPatientId(Long doctorId, Long patientId);
    void deleteById(Long id);
}
