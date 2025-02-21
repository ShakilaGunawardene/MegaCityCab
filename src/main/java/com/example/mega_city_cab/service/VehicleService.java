package com.example.mega_city_cab.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mega_city_cab.entity.Vehicle;
@Service
public interface VehicleService {

    List<Vehicle> getAllVehicles();
    Vehicle getVehicleById(String vehicleId);
    Vehicle createVehicle(Vehicle vehicle);
    Vehicle updateVehicle(String vehicleId, Vehicle vehicle);
    void deleteVehicle(String vehicleId);

    
}
