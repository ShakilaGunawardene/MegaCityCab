package com.example.mega_city_cab.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.mega_city_cab.entity.Vehicle;
import java.util.List;


@Repository
public interface VehicleRepository extends MongoRepository<Vehicle,String>{

    List<Vehicle> findByAvailable(boolean available);
    List<Vehicle> findByAssignedDriverId(String assignedDriverId);

    
}
