package com.booking.system.cs203.Booking;
import com.booking.system.cs203.entity.Booking;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@ComponentScan(basePackages = "com.booking.system.cs203")
public interface BookingService {
    void createBooking(Booking booking);
    Booking findBookingById(Long bookingId) throws BookingNotFoundException;
    List<Booking> getBookingByEmail(String email);
    List<Booking> getBookingByStoreId(Long storeId);
    List<Booking> getAllBooking();
    Booking updateBooking(Long bookingId, Booking updatedBooking) throws BookingNotFoundException;
    Booking attendedBooking(long bookingId) throws BookingNotFoundException;

}
