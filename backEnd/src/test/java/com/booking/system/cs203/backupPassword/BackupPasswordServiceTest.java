package com.booking.system.cs203.backupPassword;

import com.booking.system.cs203.entity.BackupPassword;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BackupPasswordServiceTest {
    
    @Mock
    private BackupPasswordRepository passwdRepo;
    
    @InjectMocks
    private BackupPasswordServiceImpl backupPasswordService;
    
    @Test
    void getBackupPasswordByUserEmail_ReturnsBackupPassword() {
        String email = "test@gmail.com";
        BackupPassword backupPassword = new BackupPassword();
        when(passwdRepo.findByUserEmail(email)).thenReturn(backupPassword);
        
        BackupPassword result = backupPasswordService.getBackupPasswordByUserEmail(email);
        
        assertEquals(backupPassword, result);
        verify(passwdRepo).findByUserEmail(email);
    }
    
    @Test
    void getBackupPasswordById_ReturnsBackupPassword() {
        Long id = 1L;
        BackupPassword backupPassword = new BackupPassword();
        when(passwdRepo.findById(id)).thenReturn(Optional.of(backupPassword));
        
        BackupPassword result = backupPasswordService.getBackupPasswordById(id);
        
        assertEquals(backupPassword, result);
        verify(passwdRepo).findById(id);
    }
    
    @Test
    void getBackupPasswordById_NotFound_ReturnsNull() {
        Long id = 1L;
        when(passwdRepo.findById(id)).thenReturn(Optional.empty());
        
        BackupPassword result = backupPasswordService.getBackupPasswordById(id);
        
        assertNull(result);
        verify(passwdRepo).findById(id);
    }
    
    @Test
    void addBackupPassword_AlreadyExists_ReturnsNull() {
        BackupPassword backupPassword = new BackupPassword();
        backupPassword.setId(1L);
        when(passwdRepo.findById(backupPassword.getId())).thenReturn(Optional.of(backupPassword));
        
        BackupPassword result = backupPasswordService.addBackupPassword(backupPassword);
        
        assertNull(result);
        verify(passwdRepo).findById(backupPassword.getId());
    }
    
    @Test
    void addBackupPassword_NewBackupPassword_ReturnsBackupPassword() {
        BackupPassword backupPassword = new BackupPassword();
        backupPassword.setId(1L);
        when(passwdRepo.findById(backupPassword.getId())).thenReturn(Optional.empty());
        when(passwdRepo.save(backupPassword)).thenReturn(backupPassword);
        
        BackupPassword result = backupPasswordService.addBackupPassword(backupPassword);
        
        assertEquals(backupPassword, result);
        verify(passwdRepo).save(backupPassword);
    }
    
    @Test
    void deleteBackupPassword_CallsRepositoryDelete() {
        Long id = 1L;
        
        backupPasswordService.deleteBackupPassword(id);
        
        verify(passwdRepo).deleteById(id);
    }
}
