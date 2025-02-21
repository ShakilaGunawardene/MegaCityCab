package com.example.mega_city_cab.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.mega_city_cab.entity.Admin;
import com.example.mega_city_cab.service.AdminService;

@RestController
@CrossOrigin(origins = "*")

public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/admins")
    public List<Admin>getAllAdmins(){
        return adminService.getAllAdmins();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable String id){
        Admin admin = adminService.getAdminById(id);
        return ResponseEntity.ok(admin);
    }
    @PostMapping("/createAdmin")
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin){
        Admin createdAdmin = adminService.createAdmin(admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAdmin);
    }

   
    
}
