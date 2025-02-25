package com.example.mega_city_cab.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.mega_city_cab.entity.Admin;
@Repository
public interface AdminRepository extends MongoRepository<Admin,String> {

    Optional<Admin> findByEmail(String email);

    
}
