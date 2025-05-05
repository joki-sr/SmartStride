package com.example.medicalsystem.repository;

import com.example.medicalsystem.entity.ImuData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImuDataRepository extends JpaRepository<ImuData, Long> {
    List<ImuData> findByPatientId(Long patientId);
}
