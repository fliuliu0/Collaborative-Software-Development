package com.booking.system.cs203.backupPassword;

import com.booking.system.cs203.entity.BackupPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface BackupPasswordRepository extends JpaRepository<BackupPassword, Long> {
    BackupPassword findByUserEmail(String email);
}
