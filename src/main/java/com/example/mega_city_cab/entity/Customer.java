package com.example.mega_city_cab.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "customer")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Customer {

    @Id
    private String customerId;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String city;
    private String password;
    private String role="CUSTOMER";


    
}
