package com.booking.system.cs203.backupPassword;


import com.booking.system.cs203.entity.BackupPassword;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BackupPasswordServiceImpl implements BackupPasswordService {
    
    private BackupPasswordRepository passwdRepo;
    
    @Autowired
    public BackupPasswordServiceImpl(BackupPasswordRepository passwdRepo) {
        this.passwdRepo = passwdRepo;
    }
    
    
    // methods
    @Override
    public BackupPassword getBackupPasswordByUserEmail(String email) {
        return passwdRepo.findByUserEmail(email);
    }
    
    @Override
    public BackupPassword getBackupPasswordById(Long id) {
        return passwdRepo.findById(id).orElse(null);
    }
    

    public BackupPassword addBackupPassword(BackupPassword backupPassword) {
        Optional<BackupPassword> res = passwdRepo.findById(backupPassword.getId());
        if (res.isPresent()) {
            return null;
        }
        return passwdRepo.save(backupPassword);
    }
    
    @Override
    public void deleteBackupPassword(Long id) {
        passwdRepo.deleteById(id);
    }
}
