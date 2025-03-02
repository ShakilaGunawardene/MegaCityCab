package com.example.mega_city_cab.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.mega_city_cab.entity.Customer;

@Service
public interface CustomerService {

    List<Customer> getAllCustomers();
    Customer getCustomerById(String customerId);
    ResponseEntity<?>  createCustomer(Customer customer);
    Customer updateCustomer(String customerId, Customer customer);
    void deleteCustomer(String customerId);

    
}
