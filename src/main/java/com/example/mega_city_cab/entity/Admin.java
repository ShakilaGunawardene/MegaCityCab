package com.example.mega_city_cab.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "admins")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Admin {
   
    @Id
    private String aId;
    private String adminName;
    private String email;
    private String password;
    private String role="ADMIN";
    
}
