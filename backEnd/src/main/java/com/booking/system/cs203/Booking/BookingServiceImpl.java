package com.booking.system.cs203.Booking;

import com.booking.system.cs203.User.UserService;
import com.booking.system.cs203.entity.Store;
import com.booking.system.cs203.store.StoreNotFoundException;
import com.booking.system.cs203.store.StoreRepository;
import com.booking.system.cs203.store.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.booking.system.cs203.entity.Booking;
import java.util.*;

@Service
public class BookingServiceImpl implements BookingService{
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final StoreService storeService;
    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, StoreService storeService, UserService userService){
        this.bookingRepository = bookingRepository;
        this.storeService = storeService;
        this.userService = userService;
    }
    
    @Override
    public void createBooking(Booking booking){
        List<Booking> allBookings = bookingRepository.findByStoreId(booking.getStoreId());
        int capacity = storeService.getAvailability(booking.getStoreId(), booking.getBookingDate(), booking.getBookingTime(), allBookings);
        if(capacity >= booking.getPax()){
            bookingRepository.save(booking);
            userService.updateUserBooking(booking.getEmail());
        }
        else{
            throw new BookingNotFoundException("Booking cannot be made, capacity of store cannot accommodate no. pax");
        }
        
    }
    
    @Override
    public Booking findBookingById(Long bookingId){
        Optional<Booking> b = bookingRepository.findById(bookingId);
        if(b.isPresent()){
            return b.get();
        }
        else{
            throw new BookingNotFoundException("Booking with ID " + bookingId + " not found");
        }
    }
    
    @Override
    public List<Booking> getBookingByEmail(String email){
        return bookingRepository.findByEmail(email);
    }
    
    @Override
    public List<Booking> getBookingByStoreId(Long storeId){
        return bookingRepository.findByStoreId(storeId);
    }
    
    @Override
    public List<Booking> getAllBooking(){
        return bookingRepository.findAll();
    }
    
    @Override
    public Booking updateBooking(Long bookingId, Booking updatedBooking){
        Optional<Booking> b = bookingRepository.findById(bookingId);
        if(b.isPresent()){
            Booking booking = b.get();
            booking.setBookingDate(updatedBooking.getBookingDate());
            booking.setBookingTime(updatedBooking.getBookingTime());
    //fan
            booking.setBookingPax(updatedBooking.getBookingPax());
            booking.setAttended(updatedBooking.isAttended());
            booking.setEmail(updatedBooking.getEmail());
            booking.setStoreId(updatedBooking.getStoreId());
            
            return bookingRepository.save(booking);
        }
        else{
            throw new BookingNotFoundException("Booking with ID " + bookingId + " not found");
        }
    }
    
    @Override
    public Booking attendedBooking(long bookingId){
        Optional<Booking> b = bookingRepository.findById(bookingId);
        if(b.isPresent()){
            Booking booking = b.get();
            booking.setAttended(!booking.isAttended());
            return bookingRepository.save(booking);
        }
        else{
            throw new BookingNotFoundException("Booking with ID " + bookingId + " not found");
        }
    }
    
    
}
