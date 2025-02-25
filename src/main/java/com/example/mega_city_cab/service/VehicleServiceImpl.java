package com.example.mega_city_cab.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.mega_city_cab.entity.Vehicle;
import com.example.mega_city_cab.repository.VehicleRepository;
@Service
public class VehicleServiceImpl implements VehicleService{

    @Autowired
    private VehicleRepository vehicleRepository;
    @Override
    public List<Vehicle> getAllVehicles(){
        return vehicleRepository.findAll();
    }
    @Override
    public Vehicle getVehicleById(String vehicleId){
        return vehicleRepository.findById(vehicleId).orElseThrow(() -> new RuntimeException("Vehicle not found with Id:"+ vehicleId));
    }
    @Override
    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }
    @Override
    public Vehicle updateVehicle(String vehicleId , Vehicle vehicle){
        Vehicle existingVehicle = getVehicleById(vehicleId);

        existingVehicle.setVehicleId(vehicle.getVehicleId());
        existingVehicle.setModel(vehicle.getModel());
        existingVehicle.setCategory(vehicle.getCategory());
        existingVehicle.setPassengers(vehicle.getPassengers());
        existingVehicle.setNumber(vehicle.getNumber());
        existingVehicle.setVehicleImageUrl(vehicle.getVehicleImageUrl());
        existingVehicle.setAvailable(vehicle.isAvailable());
        return vehicleRepository.save(existingVehicle);
    }
    @Override
    public void deleteVehicle(String vehicleId){
        vehicleRepository.deleteById(vehicleId);

    }
    
}
