package com.example.mega_city_cab.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.mega_city_cab.entity.Admin;
import com.example.mega_city_cab.repository.AdminRepository;
import com.example.mega_city_cab.repository.DriverRepository;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean isEmailTaken(String email) {
        return adminRepository.existsByEmail(email) ||
                driverRepository.existsByEmail(email);
    }

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
    public ResponseEntity<?> createAdmin(Admin admin) {

        if (isEmailTaken(admin.getEmail())) {
            return ResponseEntity.badRequest()
                    .body("Email already exists: " + admin.getEmail());
        }
        String encodedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodedPassword);
        return ResponseEntity.ok(adminRepository.save(admin));
    }

    @Override
    public Admin updateAdmin(String aId, Admin admin) {

        Admin existingUser = getAdminById(aId);

        existingUser.setAdminName(admin.getAdminName());
        existingUser.setEmail(admin.getEmail());
        existingUser.setPassword(admin.getPassword());

        return adminRepository.save(existingUser);

    }
  
  
    
}
