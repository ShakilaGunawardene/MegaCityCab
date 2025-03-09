package com.example.mega_city_cab.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.mega_city_cab.entity.Booking;
import com.example.mega_city_cab.entity.BookingStatus;
import com.example.mega_city_cab.entity.Driver;
import com.example.mega_city_cab.entity.Vehicle;
import com.example.mega_city_cab.exception.ResourceNotFoundException;
import com.example.mega_city_cab.repository.BookingRepository;
import com.example.mega_city_cab.repository.CustomerRepository;
import com.example.mega_city_cab.repository.DriverRepository;
import com.example.mega_city_cab.repository.VehicleRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DriverServiceImpl implements DriverService {


    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean isEmailTaken(String email) {
        return customerRepository.existsByEmail(email) || 
               driverRepository.existsByEmail(email);
    }

    @Override
    public List<Driver> getAllDrivers(){
        return driverRepository.findAll();
    }
    
    @Override
    public Driver getDriverById(String driverId) {
        return driverRepository.findById(driverId).orElse(null);
    }

    @Override
    public ResponseEntity<?> createDriver(Driver driver, Vehicle vehicle) {
         if (isEmailTaken(driver.getEmail())) {
            return ResponseEntity.badRequest()
                .body("Email already exists: " + driver.getEmail());
        }
        String encodedPassword = passwordEncoder.encode(driver.getPassword());
        driver.setPassword(encodedPassword);
        if (vehicle != null){
            Vehicle savedVehicle = vehicleRepository.save(vehicle);
            driver.setVehicleId(savedVehicle.getVehicleId());
        }

        Driver savedDriver = driverRepository.save(driver);

        return ResponseEntity.ok(savedDriver);
    }


    @Override
    public Driver updateDriver(String driverId, Driver driver) {
        Driver existingDriver = getDriverById(driverId);

        existingDriver.setName(driver.getName());
        existingDriver.setLicenseNumber(driver.getLicenseNumber());
        existingDriver.setEmail(driver.getEmail());
        existingDriver.setPhoneNumber(driver.getPhoneNumber());
        return driverRepository.save(existingDriver);
    }

    @Override
    public Driver updateAvailability(String driverId, boolean availability) {
        log.info("Updating availability to {} for driver: {}", availability, driverId);

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + driverId));

        if(!availability){
            List<Booking> activeBookings = bookingRepository.findByDriverId(driverId);
            boolean hasActiveBookings = activeBookings.stream()
                    .anyMatch(booking ->
                            booking.getStatus() == BookingStatus.CONFIRMED ||
                                    booking.getStatus() == BookingStatus.IN_PROGRESS
                    );
            if(hasActiveBookings){
                throw new IllegalStateException("Cannot update availability. Driver has active bookings");
            }
        }
        driver.setAvailable(availability);
        Driver updatedDriver = driverRepository.save(driver);
        log.info("Successfully updated availability for driver: {}", updatedDriver);
        return updatedDriver;
    }


    @Override
    public List<Booking> getDriverBookings(String driverId) {
        log.info("Fetching driver bookings for driver: {}", driverId);

        if(!driverRepository.existsById(driverId)){
            throw new ResourceNotFoundException("Driver not found with id: " + driverId);
        }

        return bookingRepository.findByDriverId(driverId);
    }

    @Override
    
    public void deleteDriver(String driverId) {
        log.info("Attempting to delete driver with ID: {}", driverId);

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + driverId));

        List<Booking> activeBookings = bookingRepository.findByDriverId(driverId);
        boolean hasActiveBookings = activeBookings.stream()
                .anyMatch(booking ->
                        booking.getStatus() == BookingStatus.CONFIRMED ||
                        booking.getStatus() == BookingStatus.IN_PROGRESS
                );
        if(hasActiveBookings){
            throw new IllegalStateException("Cannot delete driver with active bookings");
        }

        if(driver.getVehicleId() != null){
            vehicleRepository.findById(driver.getVehicleId()).ifPresent(vehicle -> {
                vehicle.setAssignedDriverId(null);
                vehicleRepository.save(vehicle);
            });
        }
        driverRepository.deleteById(driverId);
        log.info("Successfully deleted driver with ID: {}", driverId);
    }

}
