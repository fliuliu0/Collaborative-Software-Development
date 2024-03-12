package com.booking.system.cs203.Booking;

import com.booking.system.cs203.entity.Booking;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
public class BookingController {
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    public BookingController(BookingService bookingService, BookingRepository bookingRepository){
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        // ensure bookingDate is not null
        if (booking.getBookingDate() == null) {
            return ResponseEntity.badRequest().body("BookingDate cannot be null");
        }
        try{
            bookingService.createBooking(booking);
        } catch (BookingNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        return ResponseEntity.ok(booking);
    }
    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<?> findBookingById(@PathVariable Long bookingId){
        try{
            Booking booking = bookingService.findBookingById(bookingId);
            return ResponseEntity.ok(booking);
        }catch(BookingNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/bookings/user/{email}")
    public List<Booking> getBookingByEmail(@PathVariable String email){
        return bookingService.getBookingByEmail(email);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/bookings/store/{storeId}")
    public List<Booking> getBookingByStoreId(@PathVariable Long storeId){
        return bookingService.getBookingByStoreId(storeId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/bookings")
    public List<Booking> getAllBooking(){
        return bookingService.getAllBooking();
    }

    @PutMapping("/bookings/{bookingId}")
    public ResponseEntity<?> updateBooking(@PathVariable Long bookingId, @RequestBody Booking updatedBooking){
        try{
            Booking booking = bookingService.updateBooking(bookingId, updatedBooking);
            return ResponseEntity.ok(booking);
        }catch(BookingNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/admin/bookings/{bookingId}")
    public ResponseEntity<?> attendedBooking(@PathVariable Long bookingId){
        try{
            Booking booking = bookingService.attendedBooking(bookingId);
            return ResponseEntity.ok(booking);
        }catch(BookingNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<?> deleteExpenditure(@PathVariable Long bookingId) {
        try {
            Booking b = bookingService.findBookingById(bookingId);
            bookingRepository.delete(b);
            return ResponseEntity.ok("Booking deleted successfully!");
        } catch (BookingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking deleted unsuccessfully. " + e.getMessage());
        }
    }

}
