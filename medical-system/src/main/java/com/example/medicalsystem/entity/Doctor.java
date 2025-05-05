package com.example.medicalsystem.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    private String hospital;
    private String department;
}
