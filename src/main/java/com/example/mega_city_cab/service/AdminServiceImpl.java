package com.example.mega_city_cab.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mega_city_cab.entity.Admin;
import com.example.mega_city_cab.repository.AdminRepository;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
    @Override
    public Admin getAdminById(String aId) {
        return adminRepository.findById(aId)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + aId));
    }

    @Override
    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public Admin updateAdmin(String aId, Admin adminDetails) {
        Admin existingAdmin = getAdminById(aId);

        existingAdmin.setAdminName(adminDetails.getAdminName());
        existingAdmin.setEmail(adminDetails.getEmail());
        existingAdmin.setPassword(adminDetails.getPassword());

        return adminRepository.save(existingAdmin);
    }


  
  
    
}
