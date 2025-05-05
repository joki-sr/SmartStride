package com.example.medicalsystem.dto;

import lombok.Data;

@Data
public class DoctorPatientRelationDTO {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String patientName;
}
