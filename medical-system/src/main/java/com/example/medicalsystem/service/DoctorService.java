package com.example.medicalsystem.service;

import com.example.medicalsystem.dto.DoctorDTO;
import com.example.medicalsystem.dto.DoctorRegistrationDTO;
import com.example.medicalsystem.exception.ConflictException;

import java.util.List;

public interface DoctorService {
    /**
     * 获取所有医生列表
     * @return 医生列表
     */
    List<DoctorDTO> getAllDoctors();
    
    /**
     * 注册新医生
     * @param doctorDTO 医生注册信息
     * @return 注册成功的医生信息
     */
    DoctorDTO registerDoctor(DoctorRegistrationDTO doctorDTO) throws ConflictException;
}
