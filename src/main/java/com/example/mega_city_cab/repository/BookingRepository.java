package com.example.mega_city_cab.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.mega_city_cab.entity.Booking;
import com.example.mega_city_cab.entity.BookingStatus;

public interface BookingRepository extends MongoRepository<Booking, String>   {

    List<Booking> findByCustomerId(String customerId);
    List<Booking> findByDriverId(String driverId);
    List<Booking> findByStatusAndPickupDateBefore(BookingStatus status , String dateTime);
    List<Booking> findByCarIdAndStatus(String carId, BookingStatus status);

    
    @Query("{'carId':?0, 'pickupDate':{$gte:?1, $lte:?2},'status':{$in:['CONFIRMED','IN_PROGRESS']}}")
    List<Booking> findOverlappingBookings(String carId,LocalDateTime start,LocalDateTime end);

    
}
