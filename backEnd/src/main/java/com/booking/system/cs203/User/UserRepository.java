package com.booking.system.cs203.User;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.booking.system.cs203.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    boolean existsByMembershipIdAndEmailIsNot(Long newMembershipId, String email);
    //void deleteByUserId(Long userId);
    // You can define custom query methods here if needed
}
