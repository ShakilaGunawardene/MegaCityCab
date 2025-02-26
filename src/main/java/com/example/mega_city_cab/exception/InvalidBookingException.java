package com.example.mega_city_cab.exception;

public class InvalidBookingException extends RuntimeException{

    public InvalidBookingException (String message){
        super(message);
    }
    
}
