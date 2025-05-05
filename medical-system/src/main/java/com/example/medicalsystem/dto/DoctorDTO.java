package com.example.medicalsystem.dto;

import lombok.Data;

@Data
public class DoctorDTO {
    private Long id;
    private String username;
    private String name;
    private String phone;
    private String hospital;
    private String department;
}
