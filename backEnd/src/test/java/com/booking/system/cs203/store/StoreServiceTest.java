package com.booking.system.cs203.store;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.never;
import com.booking.system.cs203.entity.Booking;
import com.booking.system.cs203.entity.Store;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {
    
    @Mock
    private StoreRepository storeRepository;
    
    @InjectMocks
    private StoreServiceImpl storeService;
    
    @Test
    void getAllStores_ReturnsAllStores() {
        // Arrange
        Store store1 = new Store();
        Store store2 = new Store();
        List<Store> storeList = Arrays.asList(store1, store2);
        
        when(storeRepository.findAll()).thenReturn(storeList);
        
        // Act
        List<Store> stores = storeService.getAllStores();
        
        // Assert
        assertEquals(2, stores.size());
        
        // Verification
        verify(storeRepository).findAll();
    }
    
    @Test
    void getStoreById_ReturnsStore() {
        Long id = 1L;
        Store store = new Store();
        
        when(storeRepository.findById(id)).thenReturn(Optional.of(store));
        Store fetchedStore = storeService.getStoreById(id);
        
        assertEquals(store, fetchedStore);
        verify(storeRepository).findById(id);
    }
    
    @Test
    void getAvailability_ReturnsCorrectAvailability() {
        Long storeId = 1L;
        int initialCapacity = 10;
        Store store = new Store();
        store.setCapacity(initialCapacity);
        Date date = Date.valueOf("2023-10-25");
        Time time = Time.valueOf("10:00:00");
        
        Booking booking1 = new Booking();
        booking1.setPax(3);
        booking1.setBookingDate(date);
        booking1.setBookingTime(time);
        
        Booking booking2 = new Booking();
        booking2.setPax(2);
        booking2.setBookingDate(date);
        booking2.setBookingTime(time);
        
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        
        int availability = storeService.getAvailability(storeId, date, time, Arrays.asList(booking1, booking2));
        
        assertEquals(5, availability);
        verify(storeRepository).findById(storeId);
    }
}