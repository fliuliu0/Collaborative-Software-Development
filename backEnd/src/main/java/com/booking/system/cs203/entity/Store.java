package com.booking.system.cs203.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import com.booking.system.cs203.entity.Booking;
import com.booking.system.cs203.entity.Expenditure;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Data
@Table(name = "Store")
public class Store {
    
    // Lombok setters and getters used - same writing default setters and getter
    
    @Getter
    @Id
    private Long storeId;
    
    @Getter
    @Setter
    @Column(nullable = false)
    private String name;
    
    @Getter
    @Setter
    @Column(nullable = false)
    private String location;
    
    @Getter
    @Setter
    @Column(nullable = false)
    private int capacity;
    
    @Getter
    @Setter
    @Column(nullable = false)
    private String description;
    
    // Associative relationship
    // mapped by "store" variable in other entities
    // EAGER - loads all bookings when store is loaded
    //@Getter
    //@Setter
    //@JsonManagedReference("bookingStore")
    //@OneToMany(mappedBy = "store", fetch = FetchType.EAGER)
    //private List<Booking> bookings;
    
    //@OneToMany(mappedBy = "store" ,fetch = FetchType.EAGER)
    //@JsonManagedReference("expenditureStore")
    //private List<Expenditure> expenditure;

    public Store () {
    }
    public Store( Long storeId,String name, String location, int capacity, String description) {
        this.storeId = storeId;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.description = description;
    }
    
}
