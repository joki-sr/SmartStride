package com.example.medicalsystem.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "doctor_patient_relations")
public class DoctorPatientRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
