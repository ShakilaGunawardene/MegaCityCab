package com.example.mega_city_cab.repository;



import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.mega_city_cab.entity.Driver;

public interface DriverRepository extends MongoRepository<Driver,String>{

    
    
}
