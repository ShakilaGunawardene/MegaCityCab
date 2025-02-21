package com.example.mega_city_cab.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.mega_city_cab.entity.Customer;
import com.example.mega_city_cab.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService{


    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();

    }
    @Override
    public Customer getCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
    }
    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(String customerId, Customer customer) {
        Customer existingCustomer = getCustomerById(customerId);

        existingCustomer.setName(customer.getName());
        existingCustomer.setAddress(customer.getAddress());
        existingCustomer.setPhone(customer.getPhone());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setCity(customer.getCity());
        customer.setPassword(customer.getPassword());

        return customerRepository.save(existingCustomer);
    }


    @Override
    public void deleteCustomer(String customerId) {
        customerRepository.deleteById(customerId);
    }
    
}
