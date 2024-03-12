package com.booking.system.cs203.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "Expenditure")
@AllArgsConstructor
@NoArgsConstructor
public class Expenditure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenditureId;

    @Column(nullable = false)
    private java.sql.Date date;

    @Column(nullable = false)
    private double amount;

    private Long memberId;
    
    // One store
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference("expenditureStore")
    @JoinColumn(name = "storeId", referencedColumnName = "storeId")
    private Store store;

    public Expenditure(java.sql.Date date, double amount, Long memberId, Store store) {
        this.date = date;
        this.amount = amount;
        this.memberId = memberId;
        this.store = store;
    }
    

    
}
