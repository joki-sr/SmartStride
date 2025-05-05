package com.example.medicalsystem.repository;

import com.example.medicalsystem.entity.PatientReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientReportRepository extends JpaRepository<PatientReport, Long> {
    List<PatientReport> findByPatientIdOrderByDateDesc(Long patientId);
}
