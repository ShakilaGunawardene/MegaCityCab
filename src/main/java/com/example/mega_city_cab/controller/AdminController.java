package com.example.mega_city_cab.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mega_city_cab.entity.Admin;
import com.example.mega_city_cab.service.AdminService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/admin")

public class AdminController {

   @Autowired
    private AdminService adminService;

    @GetMapping("/viewAdmins")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        List<Admin> admins = adminService.getAllAdmins();
        return new ResponseEntity<>(admins, HttpStatus.OK);
    }

    @GetMapping("/{aId}")
    public ResponseEntity<Admin> getAdminById(@PathVariable String aId) {
        Admin admin = adminService.getAdminById(aId);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    @PostMapping("/createAdmin")
    public ResponseEntity<?> createAdmin(@RequestBody Admin admin) {
        return adminService.createAdmin(admin);
    }

   
    
}
