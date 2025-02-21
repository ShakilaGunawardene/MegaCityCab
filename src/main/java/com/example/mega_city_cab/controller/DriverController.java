package com.example.mega_city_cab.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.mega_city_cab.entity.Driver;
import com.example.mega_city_cab.service.DriverService;

public class DriverController {
     @Autowired
    private DriverService driverService;

    @GetMapping("/drivers")
    public List<Driver> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @GetMapping("/getDriverById/{id}")
    public ResponseEntity<Driver> getDriverById(@PathVariable String driverId) {
        Driver driver = driverService.getDriverById(driverId);
        return ResponseEntity.ok(driver);
    }

    @PostMapping("/createDriver")
    public ResponseEntity<Driver> createCustomer(@RequestBody Driver driver) {
        Driver createDriver = driverService.createDriver(driver);
        return ResponseEntity.status(HttpStatus.CREATED).body(createDriver);
    }

    @PutMapping("/updateDriver/{Id}")
    public ResponseEntity<Driver> upadateDriver(@PathVariable String driverId, @RequestBody Driver driver) {
        Driver updatedDriver = driverService.updateDriver(driverId, driver);
        return ResponseEntity.ok(updatedDriver);
    }

    @DeleteMapping("/deleteDriver/{Id}")
    public ResponseEntity<String> deleteDriver(@PathVariable String driverId) {
        driverService.deleteDriver(driverId);
        return ResponseEntity.ok("Driver deleted successfully");

    }
}
