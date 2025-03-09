package com.example.mega_city_cab.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.mega_city_cab.entity.Booking;
import com.example.mega_city_cab.entity.Driver;
import com.example.mega_city_cab.entity.Vehicle;

@Service
public interface DriverService {
    List<Driver> getAllDrivers();
    Driver getDriverById(String driverId);
    ResponseEntity<?> createDriver(Driver driver, Vehicle vehicle);
    Driver updateDriver(String driverId, Driver driver);
    void deleteDriver(String driverId);
    Driver updateAvailability (String driverId, boolean availability);
    List<Booking> getDriverBookings(String driverId);
    

    
}
