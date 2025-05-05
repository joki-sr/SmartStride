package com.example.medicalsystem.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String name;
    private boolean isAdmin;
    private boolean isDoctor;
    private boolean isPatient;
}
