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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mega_city_cab.entity.Customer;
import com.example.mega_city_cab.service.CustomerService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/customer")


public class CustomerController {
    
    @Autowired
    private CustomerService customerService;

    @GetMapping("/viewCustomers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return new ResponseEntity<>(customerService.getAllCustomers(), HttpStatus.OK);
    }

    @GetMapping("/getCustomer/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") String customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
    }

    @PostMapping("/createCustomer")
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @PutMapping("/updateCustomer/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") String customerId, @RequestBody Customer customer) {
        try {
            Customer updatedCustomer = customerService.updateCustomer(customerId, customer);
            return ResponseEntity.ok(updatedCustomer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteCustomer/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") String customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
 
}
