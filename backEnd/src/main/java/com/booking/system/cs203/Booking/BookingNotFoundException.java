package com.booking.system.cs203.Booking;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookingNotFoundException extends RuntimeException{
    public static final long serialVersionUID = 1L;
    public BookingNotFoundException(String message){
        super(message);
    }
    public BookingNotFoundException(Long id) {
        super("Could not find Expenditure with id: " + id);
    }
}
