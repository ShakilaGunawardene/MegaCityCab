package com.example.mega_city_cab.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.mega_city_cab.entity.Vehicle;

public interface VehicleRepository extends MongoRepository<Vehicle,String>{

    

    
}
