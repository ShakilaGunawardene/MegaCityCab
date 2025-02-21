package com.example.mega_city_cab.service;

import java.util.List;


import org.springframework.stereotype.Service;

import com.example.mega_city_cab.entity.Admin;

@Service
public interface AdminService {

   
    List<Admin> getAllAdmins();
    Admin getAdminById(String id);
    Admin createAdmin(Admin admin);
    Admin updateAdmin(String id,Admin admin);
    
}
