package com.example.mega_city_cab.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mega_city_cab.DTO.BookingRequestDTO;
import com.example.mega_city_cab.DTO.CancellationRequest;
import com.example.mega_city_cab.entity.Booking;
import com.example.mega_city_cab.entity.Customer;
import com.example.mega_city_cab.exception.ResourceNotFoundException;
import com.example.mega_city_cab.repository.CustomerRepository;
import com.example.mega_city_cab.service.BookingService;

import lombok.extern.slf4j.Slf4j;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/booking")
@Slf4j
public class BookingController {
    
    @Autowired
    private BookingService bookingService;

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/createbooking")
    public ResponseEntity<Booking> createBooking(
            @AuthenticationPrincipal UserDetails userDetails,
            @Validated @RequestBody BookingRequestDTO bookingRequest) {

        String email = userDetails.getUsername();
        log.info("Create new booking for customer email: {}", email);

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with email:" + email));

        bookingRequest.setCustomerId(customer.getCustomerId());
        Booking booking = bookingService.createBooking(bookingRequest);

        return ResponseEntity.ok(booking);
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Booking> cancelBooking(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String bookingId,
            @Validated @RequestBody CancellationRequest cancellationRequest) {
        String email = userDetails.getUsername();
        log.info("Cancelling booking: {} for customer email : {} ", bookingId, email);

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with email:" + email));

        cancellationRequest.setBookingId(bookingId);
        Booking cancelledBooking = bookingService.cancelBooking(customer.getCustomerId(), cancellationRequest);
        return ResponseEntity.ok(cancelledBooking);

    }

    @GetMapping("/getallBookings")
    public ResponseEntity<List<Booking>> getCustomerBookings(
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        log.info("Fetching bookings for customer email {}", email);

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with email:" + email));

        List<Booking> bookings = bookingService.getCustomerBookings(customer.getCustomerId());
        return ResponseEntity.ok(bookings);

    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getCustomerBooking(
        @AuthenticationPrincipal UserDetails userDetails ,
        @PathVariable String bookingId
    ){

        String email = userDetails.getUsername();
        log.info("fetching booking: {} for customer email: {}", bookingId, email);

        Customer customer = customerRepository.findByEmail(email)
                    .orElseThrow(()-> new ResourceNotFoundException("Customer not found with email :"+ email));

        Booking booking = bookingService.getBookingDetails(customer.getCustomerId(), bookingId);

        return ResponseEntity.ok(booking);

    }

    @DeleteMapping("/delete/{bookingId}")
    public ResponseEntity<Void> deleteBooking(

        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable String bookingId){

            String email = userDetails.getUsername();
            log.info("Deleting booking: {} for customer email: {} ", bookingId,email);

            Customer customer = customerRepository.findByEmail(email)
                    .orElseThrow(()-> new ResourceNotFoundException("Customer not found with email :" + email));

            bookingService.deleteBooking(customer.getCustomerId(), bookingId);
            return ResponseEntity.noContent().build();
                    


        }
}
