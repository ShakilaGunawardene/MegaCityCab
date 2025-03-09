package com.example.mega_city_cab.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document( collection = "vehicle")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    @Id
    private String vehicleId;
    private String model;
    private String category;
    private Integer passengers;
    private String number;
    private String assignedDriverId;
    private String vehicleImageUrl;
    private boolean available = true;
    private double baseRate;
    private double driverRate;
    
}
