package com.example.medicalsystem.service;

import com.example.medicalsystem.dto.PatientDTO;

import java.util.List;

public interface PatientService {
    /**
     * 获取所有患者列表
     * @return 患者列表
     */
    List<PatientDTO> getAllPatients();
}
