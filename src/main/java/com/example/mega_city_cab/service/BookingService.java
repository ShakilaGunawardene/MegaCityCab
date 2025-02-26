package com.example.mega_city_cab.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mega_city_cab.DTO.BookingRequestDTO;
import com.example.mega_city_cab.DTO.CancellationRequest;
import com.example.mega_city_cab.entity.Booking;

@Service
public interface BookingService {

    List<Booking> getAllBookings();

    Booking getBookingById(String bookingId);

    Booking createBooking(BookingRequestDTO request);

    Booking cancelBooking(String customerId, CancellationRequest request);

    List<Booking> getCustomerBookings(String customerId);

    Booking getBookingDetails(String customerId, String bookingId);

    void deleteBooking(String bookingId, String customerId);
    
}
