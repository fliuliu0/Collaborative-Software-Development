package com.booking.system.cs203.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BackupPassword")
public class BackupPassword {
    
    public BackupPassword(String backupHash, User user) {
        this.backupHash = backupHash;
        this.user = user;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String backupHash;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinColumn(name = "email", referencedColumnName = "email")
    private User user;
    

}