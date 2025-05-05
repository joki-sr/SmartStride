package com.example.medicalsystem.service.impl;

import com.example.medicalsystem.dto.UserDTO;
import com.example.medicalsystem.dto.UserRegistrationDTO;
import com.example.medicalsystem.entity.Patient;
import com.example.medicalsystem.entity.User;
import com.example.medicalsystem.exception.ConflictException;
import com.example.medicalsystem.exception.UnauthorizedException;
import com.example.medicalsystem.repository.PatientRepository;
import com.example.medicalsystem.repository.UserRepository;
import com.example.medicalsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PatientRepository patientRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public UserDTO login(String username, String password) throws UnauthorizedException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("用户名或密码错误"));
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("用户名或密码错误");
        }
        
        return convertToDTO(user);
    }
    
    @Override
    @Transactional
    public UserDTO register(UserRegistrationDTO userDTO) throws ConflictException {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new ConflictException("用户名已存在");
        }
        
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setName(userDTO.getName());
        user.setPhone(userDTO.getPhone());
        user.setGender(userDTO.getGender());
        user.setBirthDate(userDTO.getBirthDate());
        user.setIdCard(userDTO.getIdCard());
        user.setPassport(userDTO.getPassport());
        user.setPatient(true); // 默认注册为患者
        
        User savedUser = userRepository.save(user);
        
        // 创建患者记录
        Patient patient = new Patient();
        patient.setUser(savedUser);
        patientRepository.save(patient);
        
        return convertToDTO(savedUser);
    }
    
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setAdmin(user.isAdmin());
        dto.setDoctor(user.isDoctor());
        dto.setPatient(user.isPatient());
        return dto;
    }
}
