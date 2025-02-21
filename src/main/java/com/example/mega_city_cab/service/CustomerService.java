package com.example.mega_city_cab.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mega_city_cab.entity.Customer;

@Service
public interface CustomerService {

    List<Customer> getAllCustomers();
    Customer getCustomerById(String id);
    Customer createCustomer(Customer customer);
    Customer updateCustomer(String id, Customer customer);
    void deleteCustomer(String id);

    
}
