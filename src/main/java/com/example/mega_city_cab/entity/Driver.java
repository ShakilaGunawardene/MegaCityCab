package com.example.mega_city_cab.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collation = "driver")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Driver {

    @Id

    private String driverId;
    private String name;
    private String licenseNumber;
    private String email;
    private String phoneNumber;
    private String role="DRIVER";
    private String password;
    private String vehicleId;
    private boolean hasOwnCar = false;
    private boolean available = true;
    
}
