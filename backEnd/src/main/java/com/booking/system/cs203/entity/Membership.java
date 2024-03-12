package com.booking.system.cs203.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@Data
@Table(name = "Membership")
public class Membership {
    @Id
    @Setter
    @Column(nullable = false, unique = true)
    private Long memberId;
    
    @Column
    @Getter
    private String tier;
//    @Column(name = "userId")
//    private Long userId;
    
//    @Column(nullable = false)
//    private java.sql.Date joinedDate;

//    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "userId", referencedColumnName = "userId")
//    @JsonBackReference
//    @Fetch(FetchMode.JOIN)
//    private User user = null;


    public Membership(){}

    public Membership(Long memberId, String tier){
        this.memberId = memberId;
        this.tier = tier;

    }

    public void setTier(String tier){
        this.tier = tier;
    }
//    public long getUserId(){
//        return userId;
//    }
//    public void setUserId(Long userId){
//        this.userId = userId;
//    }
}
