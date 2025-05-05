package com.example.medicalsystem.dto;

import lombok.Data;

import java.util.Map;

@Data
public class PatientReportDTO {
    private String date;
    private String type;
    private String summary;
    private Map<String, int[]> data;
}
