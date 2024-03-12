package com.booking.system.cs203.entity;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
@Entity
@Data
@Getter
@JsonIgnoreProperties(value = {"managedEntities"}, allowSetters = true)
//@Setter
@Table(name = "User")
public class User /*implements UserDetails*/ {

    @Id
    private String email;


    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String number;
    @Getter
    @Column(nullable = false, unique = true)
    @JsonIgnore
    private String hashPassword;

    @Column(nullable = true)
    private int bookingCount;
    @Column(nullable = true)
    private double attendedPercentage;
    @Column(nullable = true)
    private Long membershipId;
    // We define two roles/authorities: ROLE_USER or ROLE_ADMIN
    //private String authorities;
    private String roles;
    public User() {
    }
    public User(String name, String number, String email, String hashPassword, int bookingCount, double attendedPercentage,String roles, Long membershipId) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.hashPassword = hashPassword;
        this.bookingCount = bookingCount;
        this.attendedPercentage = attendedPercentage;
        this.roles = roles;
        this.membershipId = membershipId;
    }
    
    
    // Associative relationship
    // mapped by variable "user" in other entities
    // EAGER - loads all bookings when user is loaded
    
    //@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    //@JsonIgnore
    //private List<Booking> bookings;

    
    //@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    //@JsonBackReference
    //private BackupPassword backupPassword;

//    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private Membership membership;

//    @Override
//    public String getPassword() {
//        return this.hashPassword;
//    }

//    @Override
//    public String getUsername() {
//        return this.email;
//    }
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Arrays.asList(new SimpleGrantedAuthority(authorities));
//    }
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }

}
