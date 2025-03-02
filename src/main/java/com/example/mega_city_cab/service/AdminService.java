package com.example.mega_city_cab.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.mega_city_cab.entity.Admin;

@Service
public interface AdminService {

   
    List<Admin> getAllAdmins();
    Admin getAdminById(String aId);
    ResponseEntity<?> createAdmin(Admin admin);
    Admin updateAdmin(String aId,Admin admin);
    
}
