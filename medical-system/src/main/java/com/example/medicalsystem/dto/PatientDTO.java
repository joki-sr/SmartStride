package com.example.medicalsystem.dto;

import lombok.Data;

@Data
public class PatientDTO {
    private Long id;
    private String name;
    private String phone;
    private String gender;
    private String birthdate;
    private String idCard;
    private String passport;
}
