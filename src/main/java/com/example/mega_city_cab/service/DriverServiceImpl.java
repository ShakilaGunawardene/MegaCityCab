package com.example.mega_city_cab.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.mega_city_cab.entity.Driver;
import com.example.mega_city_cab.repository.DriverRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DriverServiceImpl implements DriverService {


    @Autowired
    private DriverRepository driverRepository;

    @Override
    public List<Driver> getAllDrivers(){
        return driverRepository.findAll();
    }
    @Override
    public Driver getDriverById(String driverId){
        return driverRepository.findById(driverId).orElseThrow(() -> new RuntimeException("Driver not found with Id:"+driverId));

    }

     @Override
    public Driver createDriver(Driver driver) {
        return driverRepository.save(driver);
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
    public void deleteDriver(String driverId) {
        driverRepository.deleteById(driverId);
    }


    
}
