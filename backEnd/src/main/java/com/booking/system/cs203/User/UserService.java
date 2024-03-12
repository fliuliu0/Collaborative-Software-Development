package com.booking.system.cs203.User;

import com.booking.system.cs203.dto.UserUpdateDto;
import com.booking.system.cs203.entity.User;

import java.util.*;
public interface UserService {


    List<User> getAllUsers();
    User createUser(User user);
    User updateUser(String email, User updatedUserInfo);
    User findUserByEmail(String email);
    //void changePassword(Long userId, String newPassword);
    //void resetPassword(String email, String newPassword);
    int getBookingCount(String email);
    double getAttendancePercentage(String email);
    void updateUserMember(String email, Long membershipId);

    void updateUserWithNewEmail(User existingUser, UserUpdateDto userUpdateDto);

    void updateExistingUser(User existingUser, UserUpdateDto userUpdateDto);

    void updateUserBooking(String email);

    void updateBookingsEmail(String email, String newEmail);
    void updateUserPassword(String email, String newPassword);
    int getFutureBookingCount(String email);
}
