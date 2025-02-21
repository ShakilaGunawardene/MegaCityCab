package com.example.mega_city_cab.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.mega_city_cab.entity.Customer;
import com.example.mega_city_cab.service.CustomerService;

@RestController
@CrossOrigin(origins = "*")

public class CustomerController {
    
    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/getCustomerById/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customer);
    }

    @PostMapping("/createCustomer")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer createCustomer = customerService.createCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(createCustomer);
    }

    @PutMapping("/updateCustomer/{Id}")
    public ResponseEntity<Customer> upadateCustomer(@PathVariable String customerId, @RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomer(customerId, customer);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/deleteCustomer/{Id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable String customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok("Customer deleted successfully");

    }
}
