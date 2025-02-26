package com.example.mega_city_cab.DTO;

import lombok.Data;

@Data
public class CancellationRequest {
    
    private String bookingId;
    private String reason;
}
