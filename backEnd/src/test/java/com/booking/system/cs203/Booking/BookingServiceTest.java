package com.booking.system.cs203.Booking;

import com.booking.system.cs203.entity.Booking;
import com.booking.system.cs203.store.StoreService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.never;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    
    
    // Helper methods to create Date and Time objects.
    public Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day); // Year, Month, Day
        return new Date(calendar.getTimeInMillis());
    }
    public Time createTime(int hour, int minute, int second) {
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.set(Calendar.HOUR_OF_DAY, hour); // 24-hour format
        calendarTime.set(Calendar.MINUTE, minute);
        calendarTime.set(Calendar.SECOND, second);
        return new Time(calendarTime.getTimeInMillis());
    }
    
    @Mock
    private BookingRepository bookingRepository; // Assuming you have a BookingRepository.
    
    @Mock
    private StoreService storeService;
    
    @InjectMocks
    private BookingServiceImpl bookingService; // Assuming you have a BookingServiceImpl.
    
    
    @Test
    void createBooking_SufficientCapacity_BookingCreated() {
        // Arrange
        Booking booking = new Booking(500L, createDate(2002, 2, 2), createTime(19, 0,0), false, 3, "alex@gmail.com", 1L);
        
        when(bookingRepository.findByStoreId(booking.getStoreId())).thenReturn(new ArrayList<>());
        when(storeService.getAvailability(eq(booking.getStoreId()), eq(booking.getBookingDate()),
                eq(booking.getBookingTime()), anyList())).thenReturn(20);
        
        // Act
        bookingService.createBooking(booking);
        
        // Assert
        verify(bookingRepository).save(booking);
    }
    
    @Test
    void findBookingById_BookingExists_ReturnBooking(){
        Booking booking = new Booking(500L, createDate(2002, 2, 2), createTime(19, 0,0), false, 3, "alex@gmail.com", 1L);
        Long bookingId = 500L;
        
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        
        Booking retrievedBooking = bookingService.findBookingById(bookingId);
        
        assertNotNull(retrievedBooking);
        assertEquals(booking, retrievedBooking);
        verify(bookingRepository).findById(bookingId);
    }
    
    @Test
    void findBookingById_BookingDoesNotExist_ThrowException(){
        Long bookingId = 99999L;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());
        
        try {
            bookingService.findBookingById(bookingId);
        } catch(BookingNotFoundException e) {
            assertNotNull(e);
            System.out.println(e.getMessage());
        }
        
        
        verify(bookingRepository).findById(bookingId);
    }
    
    @Test
    void getBookingByEmail_ExistingEmail_ReturnsBookings() {
        Booking booking1 = new Booking(500L, createDate(2002, 2, 2), createTime(19, 0,0), false, 3, "test@gmail.com", 1L);
        Booking booking2 = new Booking(501L, createDate(2002, 2, 2), createTime(19, 0,0), false, 3, "test@gmail.com", 1L);
        
        String email = "test@example.com";
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking1);
        bookings.add(booking2);
        
        when(bookingRepository.findByEmail(email)).thenReturn(bookings);
        List<Booking> retrievedBookings = bookingService.getBookingByEmail(email);
        
        assertNotNull(retrievedBookings);
        assertEquals(bookings.size(), retrievedBookings.size());
        verify(bookingRepository).findByEmail(email);
    }
    
    @Test
    void getBookingByStoreId_ExistingStore_ReturnsBookings() {
        Long storeId = 1L;
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking());
        
        when(bookingRepository.findByStoreId(storeId)).thenReturn(bookings);
        
        List<Booking> retrievedBookings = bookingService.getBookingByStoreId(storeId);
        
        assertNotNull(retrievedBookings);
        assertEquals(bookings.size(), retrievedBookings.size());
        verify(bookingRepository).findByStoreId(storeId);
    }
    
    @Test
    void getAllBooking_BookingsExist_ReturnsAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking());
        bookings.add(new Booking());
        bookings.add(new Booking());
        
        when(bookingRepository.findAll()).thenReturn(bookings);
        
        List<Booking> allBookings = bookingService.getAllBooking();
        
        assertNotNull(allBookings);
        assertEquals(bookings.size(), allBookings.size());
        verify(bookingRepository).findAll();
    }
    
    
    @Test
    void updateBooking_ValidUpdate_UpdatesAndReturnsBooking() {
        Long bookingId = 500L;
        Booking oldBooking = new Booking(500L, createDate(2002, 2, 2), createTime(19, 0,0), false, 3, "test@gmail.com", 1L);
        
        Date newdate = createDate(2002, 3, 20);
        Time newtime = createTime(15, 0,0);
        // Changed date and time
        Booking updatedBookingData = new Booking(500L, newdate, newtime, false, 3, "test@gmail.com", 1L);
        
        
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(oldBooking));
        when(bookingRepository.save(updatedBookingData)).thenReturn(updatedBookingData);
        
        Booking result = bookingService.updateBooking(bookingId, updatedBookingData);
        
        assertNotNull(result);
        
        // Assert that the result has the updated details.
        assertEquals(500L, result.getBookingId());

        assertEquals(newdate, result.getDate());
        assertEquals(newtime, result.getTime());


        assertEquals("test@gmail.com", result.getEmail());
        assertEquals(1L, result.getStoreId());
        
        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository).save(updatedBookingData);
    }
    
    @Test
    void updateBooking_NotFound_ThrowsError() {
        Long nonExistentBookingId = 999L;
        Booking updatedBookingData = new Booking(nonExistentBookingId, createDate(2002, 4, 25), createTime(16, 0,0), false, 4, "notfound@gmail.com", 2L);
        
        // Mock repository to return empty for the given ID
        when(bookingRepository.findById(nonExistentBookingId)).thenReturn(Optional.empty());
        
        // Try updating the non-existent booking
        try {
            bookingService.updateBooking(nonExistentBookingId, updatedBookingData);
        } catch(BookingNotFoundException e) {
            assertNotNull(e);
            assertEquals("Booking with ID " + nonExistentBookingId + " not found", e.getMessage());
            System.out.println(e.getMessage());
        }
        
        
        // Verify
        verify(bookingRepository).findById(nonExistentBookingId);
        verify(bookingRepository, never()).save(any(Booking.class));
    }
    
    @Test
    void attendedBooking_ValidId_MarksAsAttendedAndReturnsBooking() {
        Long bookingId = 500L;
        Booking booking = new Booking(500L, createDate(2002, 2, 2), createTime(19, 0,0), false, 3, "test@gmail.com", 1L);
        
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        
        Booking result = bookingService.attendedBooking(bookingId);
        
        assertNotNull(result);
        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository).save(booking);
    }

    
    
}