package com.example.mega_city_cab.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.mega_city_cab.entity.Customer;

public interface CustomerRepository extends MongoRepository<Customer,String>{

    

    
}
