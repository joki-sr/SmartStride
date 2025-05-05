package com.example.medicalsystem.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "imu_data")
public class ImuData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    
    private String timestamp;
    private String deviceId;
    private String deviceName;
    private Double accX;
    private Double accY;
    private Double accZ;
    private Double gyroX;
    private Double gyroY;
    private Double gyroZ;
    private Double roll;
    private Double pitch;
    private Double yaw;
}
