package com.example.mega_city_cab.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.mega_city_cab.entity.Driver;

@Repository
public interface DriverRepository extends MongoRepository<Driver, String> {

    Optional<Driver> findByEmail(String driverEmail);

    boolean existsByEmail(String email);

    List<Driver> findByAvailable(boolean available);

    Optional<Driver> findFirstByAvailableAndHasOwnCarFalse(boolean available);

    Optional<Driver> findByVehicleId(String vehicleId);  


    
}
