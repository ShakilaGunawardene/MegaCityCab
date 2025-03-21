package com.example.mega_city_cab.DTO;

import lombok.Data;

@Data
public class BookingRequestDTO {

    private String customerId;
    private String vehicleId;
    private String bookingId;
    private String pickupLocation;
    private String destination;
    private String pickupDate;
    private String pickupTime;
    private boolean driverRequired;
}
