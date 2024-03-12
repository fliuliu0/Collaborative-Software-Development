package com.booking.system.cs203.backupPassword;


import com.booking.system.cs203.entity.BackupPassword;

import java.util.List;

public interface BackupPasswordService {
    
    
    
    BackupPassword getBackupPasswordByUserEmail(String email);

    BackupPassword getBackupPasswordById(Long id);

    BackupPassword addBackupPassword(BackupPassword backupPassword);

    void deleteBackupPassword(Long id);
}
