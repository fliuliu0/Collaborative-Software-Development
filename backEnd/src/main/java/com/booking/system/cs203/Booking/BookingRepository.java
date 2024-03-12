package com.booking.system.cs203.Booking;

import com.booking.system.cs203.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.*;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByEmail(String email);
    List<Booking> findByStoreId(Long storeId);
    List<Booking> findByStoreIdAndDate(Long storeId, Date date);

    int countByEmailAndAttended(String email, boolean b);
}
