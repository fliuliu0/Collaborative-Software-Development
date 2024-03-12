package com.booking.system.cs203.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.sql.Time;

@Entity
@Data
@Table(name = "Booking")
@AllArgsConstructor
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @Column(nullable = false)
    private java.sql.Date date;

    @Column(nullable = false)
    private java.sql.Time time;
    
    @Column()
    private boolean attended;
    
    // Associative relationship
    // One user many bookings
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JsonBackReference("bookingUser")
    //@JoinColumn(name = "email", referencedColumnName = "email")

    private int pax;
    private String email;

    // One store many bookings
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JsonBackReference("bookingStore")
    //@JoinColumn(name = "storeId", referencedColumnName = "storeId")
    private Long storeId;

    public Booking() {
    }

    public Date getBookingDate() {
        return date;
    }
    public Time getBookingTime() {
        return time;
    }
    
//fan
    public int getBookingPax() {
        return pax;
    }

    public void setBookingDate(Date date) {
        this.date = date;
    }

    public void setBookingTime(Time time) {
        this.time = time;
    }

    public void setBookingPax(int pax) {
        this.pax = pax;
    }
  
    public Booking ( java.sql.Date date, java.sql.Time time, boolean attended, int pax, String email, Long storeId) {
        this.date = date;
        this.time = time;
        this.attended = attended;
        this.pax = pax;
        this.email = email;
        this.storeId = storeId;
    }
    
}
