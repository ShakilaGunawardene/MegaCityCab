package com.example.mega_city_cab.DTO;

import lombok.Data;

@Data
public class BookingRequestDTO {

    private String customerId;
    private String vehicleId;
    private String pickupLocation;
    private String dropLocation;
    private String pickupDate;
    private boolean driverRequired;
    
}
