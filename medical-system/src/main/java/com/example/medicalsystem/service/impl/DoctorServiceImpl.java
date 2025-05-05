package com.example.medicalsystem.service.impl;

import com.example.medicalsystem.dto.DoctorDTO;
import com.example.medicalsystem.dto.DoctorRegistrationDTO;
import com.example.medicalsystem.entity.Doctor;
import com.example.medicalsystem.entity.User;
import com.example.medicalsystem.exception.ConflictException;
import com.example.medicalsystem.repository.DoctorRepository;
import com.example.medicalsystem.repository.UserRepository;
import com.example.medicalsystem.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {
    
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public DoctorDTO registerDoctor(DoctorRegistrationDTO doctorDTO) throws ConflictException {
        if (userRepository.existsByUsername(doctorDTO.getUsername())) {
            throw new ConflictException("用户名已存在");
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(doctorDTO.getUsername());
        user.setPassword(passwordEncoder.encode(doctorDTO.getPassword()));
        user.setName(doctorDTO.getName());
        user.setPhone(doctorDTO.getPhone());
        user.setDoctor(true);
        
        User savedUser = userRepository.save(user);
        
        // 创建医生
        Doctor doctor = new Doctor();
        doctor.setUser(savedUser);
        doctor.setHospital(doctorDTO.getHospital());
        doctor.setDepartment(doctorDTO.getDepartment());
        
        Doctor savedDoctor = doctorRepository.save(doctor);
        
        return convertToDTO(savedDoctor);
    }
    
    private DoctorDTO convertToDTO(Doctor doctor) {
        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setUsername(doctor.getUser().getUsername());
        dto.setName(doctor.getUser().getName());
        dto.setPhone(doctor.getUser().getPhone());
        dto.setHospital(doctor.getHospital());
        dto.setDepartment(doctor.getDepartment());
        return dto;
    }
}
