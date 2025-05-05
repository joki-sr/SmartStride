package com.example.medicalsystem.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "patient_reports")
public class PatientReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    
    private String date;
    private String type;
    private String summary;
    
    @Column(columnDefinition = "TEXT")
    private String data; // JSON格式存储
}
