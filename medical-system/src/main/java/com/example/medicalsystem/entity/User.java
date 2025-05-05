package com.example.medicalsystem.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String username;
    
    private String password;
    private String name;
    private String phone;
    private boolean isAdmin;
    private boolean isDoctor;
    private boolean isPatient;
    private String gender;
    
    @Column(name = "birth_date")
    private String birthDate;
    
    @Column(name = "id_card")
    private String idCard;
    
    private String passport;
}
