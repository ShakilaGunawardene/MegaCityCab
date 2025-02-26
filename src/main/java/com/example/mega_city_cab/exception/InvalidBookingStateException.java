package com.example.mega_city_cab.exception;

public class InvalidBookingStateException extends RuntimeException {

    public InvalidBookingStateException(String message){
        super(message);
    }
    
}
