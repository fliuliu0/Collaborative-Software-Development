package com.booking.system.cs203.Expenditure;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.booking.system.cs203.entity.Expenditure;

import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;
import java.util.List;


import java.sql.Date;
import java.time.LocalDateTime;

import java.util.Calendar;
import java.util.Optional;

import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)

public class ExpenditureTest {
    public Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day); // Year, Month, Day
        return new Date(calendar.getTimeInMillis());
    }
    
    
    @InjectMocks
    private ExpenditureServiceImpl expenditureService;
    
    @Mock
    private ExpenditureRepository expenditureRepository;
    
    @Test
    public void createExpenditure_SavesExpenditure() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        int year = currentDateTime.getYear();
        Expenditure expenditure = new Expenditure(1L, createDate(year, 3, 3), 20000.0, 500L, null);
        
        
        expenditureService.createExpenditure(expenditure);
        
        verify(expenditureRepository).save(expenditure);
    }
    
    @Test
    public void findExpenditureById_ExpenditureExists_ReturnsExpenditure() {
        Long id = 1L;
        LocalDateTime currentDateTime = LocalDateTime.now();
        int year = currentDateTime.getYear();
        Expenditure expenditure = new Expenditure(id, createDate(year, 3, 3), 20000.0, 500L, null);
        
        
        when(expenditureRepository.findById(id)).thenReturn(Optional.of(expenditure));
        
        Expenditure result = expenditureService.findExpenditureById(id);
        
        assertEquals(expenditure, result);
        verify(expenditureRepository).findById(id);
    }
    
    @Test
    public void findExpenditureById_ExpenditureNotExists_ThrowsException() {
        Long id = 1L;
        when(expenditureRepository.findById(id)).thenReturn(Optional.empty());
        
        assertThrows(ExpenditureNotFoundException.class, () -> {
            expenditureService.findExpenditureById(id);
        });
        verify(expenditureRepository).findById(id);
    }
    
    @Test
    public void getAllExpenditureByMemberId_ReturnsExpenditures() {
        Long memberId = 1L;
        List<Expenditure> mockExpenditures = Arrays.asList(new Expenditure(), new Expenditure());
        
        when(expenditureRepository.findByMemberId(memberId)).thenReturn(mockExpenditures);
        
        List<Expenditure> returnedExpenditures = expenditureService.getAllExpenditureByMemberId(memberId);
        
        assertEquals(mockExpenditures.size(), returnedExpenditures.size());
        verify(expenditureRepository).findByMemberId(memberId);
    }
    
    @Test
    public void getAllExpenditureByStoreId_ReturnsExpenditures() {
        Long storeId = 1L;
        List<Expenditure> mockExpenditures = Arrays.asList(new Expenditure(), new Expenditure());
        
        when(expenditureRepository.findByStoreStoreId(storeId)).thenReturn(mockExpenditures);
        
        List<Expenditure> returnedExpenditures = expenditureService.getAllExpenditureByStoreId(storeId);
        
        assertEquals(mockExpenditures.size(), returnedExpenditures.size());
        verify(expenditureRepository).findByStoreStoreId(storeId);
    }
    @Test
    public void updateExpenditure_ExpenditureExists_UpdatesAndReturnsExpenditure() {
        Long id = 1L;
        Expenditure oldExpenditure = new Expenditure();
        Expenditure updatedExpenditure = new Expenditure();
        updatedExpenditure.setAmount(100.0);
        
        when(expenditureRepository.findById(id)).thenReturn(Optional.of(oldExpenditure));
        when(expenditureRepository.save(any(Expenditure.class))).thenReturn(updatedExpenditure);
        
        Expenditure result = expenditureService.updateExpenditure(id, updatedExpenditure);
        
        assertEquals(updatedExpenditure.getAmount(), result.getAmount());
        verify(expenditureRepository).findById(id);
        verify(expenditureRepository).save(any(Expenditure.class));
    }
    
    @Test
    public void updateExpenditure_ExpenditureNotExists_ThrowsException() {
        Long id = 1L;
        Expenditure updatedExpenditure = new Expenditure();
        when(expenditureRepository.findById(id)).thenReturn(Optional.empty());
        
        assertThrows(ExpenditureNotFoundException.class, () -> {
            expenditureService.updateExpenditure(id, updatedExpenditure);
        });
        verify(expenditureRepository).findById(id);
    }
}