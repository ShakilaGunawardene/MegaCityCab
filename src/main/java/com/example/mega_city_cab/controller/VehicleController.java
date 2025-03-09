package com.example.mega_city_cab.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import com.example.mega_city_cab.entity.Vehicle;
import com.example.mega_city_cab.service.CloudinaryService;
import com.example.mega_city_cab.service.VehicleService;

@RestController
@CrossOrigin(origins = "*")

public class VehicleController {

     @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/all/viewVehicle")
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable String vehicleId) {
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
        return new ResponseEntity<>(vehicleService.getVehicleById(vehicleId), HttpStatus.OK);
    }

     @PostMapping("/auth/cars/createVehicle")
    public ResponseEntity<Vehicle> createVehicle(
            @RequestParam String brand,
            @RequestParam String model,
            @RequestParam String category,
            @RequestParam Integer passengers,
            @RequestParam String number,
            @RequestParam MultipartFile vehicleImg) throws IOException {

        String vehicleImage = cloudinaryService.uploadImage(vehicleImg);

        Vehicle vehicle = new Vehicle();
        vehicle.setModel(model);
        vehicle.setCategory(category);
        vehicle.setPassengers(passengers);
        vehicle.setNumber(number);
        vehicle.setVehicleImageUrl(vehicleImage);
        

        Vehicle savedVehicle = vehicleService.createVehicle(vehicle);
        return ResponseEntity.ok(savedVehicle);
    } 

    @PutMapping("/updateVehicle/{vehicleId}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable String vehicleId, @RequestBody Vehicle vehicle) {
        Vehicle updatedVehicle = vehicleService.updateVehicle(vehicleId, vehicle);
        return new ResponseEntity<>(updatedVehicle, HttpStatus.OK);
    }

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<String> deleteVehicle(@PathVariable String vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    
    
}
