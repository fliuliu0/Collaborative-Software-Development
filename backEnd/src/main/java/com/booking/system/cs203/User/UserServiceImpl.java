package com.booking.system.cs203.User;

import com.booking.system.cs203.Booking.BookingRepository;
import com.booking.system.cs203.dto.UserUpdateDto;
import com.booking.system.cs203.entity.Booking;
import com.booking.system.cs203.entity.Membership;
import com.booking.system.cs203.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.booking.system.cs203.Membership.MemberService;
import com.booking.system.cs203.Membership.MemberNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private MemberService membership;

    private UserRepository userRepository;

    private BookingRepository bookingRepository;


    private PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder, BookingRepository bookingRepository, MemberService membership) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.bookingRepository = bookingRepository;
        this.membership = membership;
    }



    
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser( User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserEmailExistException(user.getEmail());
        }

        // Encrypt the user's password
        //String rawPassword = user.getHashPassword();
        //if(rawPassword != null){

            String password = user.getHashPassword();
            
            user.setHashPassword(password);
        //}

        return userRepository.save(user);
        //return null;
    }

    @Override
    public User updateUser(String email, User updatedUserInfo){
        Optional<User> u = userRepository.findByEmail(email);
        if (u.isPresent()) {
            User user = u.get();
            user.setName(updatedUserInfo.getName());
            user.setEmail(updatedUserInfo.getEmail());
            user.setNumber(updatedUserInfo.getNumber());
            return userRepository.save(user);
        } else {
            throw new UserNotFoundException(email);
        }
    }
    @Override
    public void updateBookingsEmail(String oldEmail, String newEmail) {
        List<Booking> bookings = bookingRepository.findByEmail(oldEmail);
        for (Booking booking : bookings) {
            // Update the booking's email
            booking.setEmail(newEmail);
        }
        bookingRepository.saveAll(bookings);
    }
    @Override
    public void updateUserWithNewEmail(User existingUser, UserUpdateDto userUpdateDto) {
        User newUser = new User();
        String newEmail = userUpdateDto.getEmail();
        String oldEmail = existingUser.getEmail();
        newUser.setEmail(newEmail);
        updateBookingsEmail(oldEmail, newEmail);


        newUser.setName(userUpdateDto.getName() != null ? userUpdateDto.getName() : existingUser.getName());
        newUser.setNumber(userUpdateDto.getNumber() != null ? userUpdateDto.getNumber() : existingUser.getNumber());
        newUser.setAttendedPercentage(existingUser.getAttendedPercentage());
        newUser.setBookingCount(existingUser.getBookingCount());
        newUser.setRoles(existingUser.getRoles());
        newUser.setMembershipId(existingUser.getMembershipId());

        String newPassword = userUpdateDto.getHashPassword();
        String oldPassword = existingUser.getHashPassword();

        if (newPassword != null && !newPassword.isEmpty()) {
            newUser.setHashPassword(encoder.encode(newPassword));
        } else {
            newUser.setHashPassword(oldPassword);
        }

        userRepository.delete(existingUser);
        userRepository.save(newUser);

    }

    @Override
    public void updateExistingUser(User existingUser, UserUpdateDto userUpdateDto) {
        if (userUpdateDto.getName() != null) {
            existingUser.setName(userUpdateDto.getName());
        }
        if (userUpdateDto.getNumber() != null) {
            existingUser.setNumber(userUpdateDto.getNumber());
        }
        if (userUpdateDto.getAttendedPercentage() != 0.0) {
            existingUser.setAttendedPercentage(userUpdateDto.getAttendedPercentage());
        }
        if (userUpdateDto.getBookingCount() != 0) {
            existingUser.setBookingCount(userUpdateDto.getBookingCount());
        }

        String newPassword = userUpdateDto.getHashPassword();
        if (newPassword != null && !newPassword.isEmpty()) {
            existingUser.setHashPassword(encoder.encode(newPassword));
        }

        userRepository.save(existingUser);

    }

    @Override
    public User findUserByEmail(String email) {
        Optional<User> u = userRepository.findByEmail(email);
        if (u.isPresent()) {
            return u.get();
        } else {
            throw new UserNotFoundException(email);
        }
    }

//    implement changePassword and resetPassword at later stage

//    @Override
//    public void changePassword(Long userId, String newPassword) {
//
//    }
//
//    @Override
//    public void resetPassword(String email, String newPassword) {
//
//    }

    @Override
    public int getBookingCount(String email) {
        // call bookingrepository to get all bookings by user
        List<Booking> bookings = bookingRepository.findByEmail(email);
        return bookings.size();
    }

    @Override
    public double getAttendancePercentage(String email) {
        // get total booking by colling getBookingCount
        int totalBookings = getBookingCount(email);

        // query to count total bookings by email and attended
        int attendedBookings = bookingRepository.countByEmailAndAttended(email, true);
        if (totalBookings == 0 || attendedBookings == 0) {
            return 0.0;
        }

        return (double) attendedBookings / totalBookings * 100;
    }

    public void updateUserMember(String email, Long newMembershipId) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Long currentMembershipId = user.getMembershipId();

            // If the new membershipId is different from the current one, check if it's valid.
            if (!newMembershipId.equals(currentMembershipId)) {
                // Check if the new membershipId exists.
                Optional<Membership> newMembershipOptional = Optional.ofNullable(membership.findMemberById(newMembershipId));

                if (newMembershipOptional.isPresent()) {
                    // Check if the new membershipId is not already used by another user.
                    boolean isMembershipIdUsedByOtherUser = userRepository.existsByMembershipIdAndEmailIsNot(newMembershipId, email);

                    if (!isMembershipIdUsedByOtherUser) {
                        // Update the user's membershipId.
                        user.setMembershipId(newMembershipId);
                        userRepository.save(user);
                    } else {
                        throw new MemberNotFoundException("MembershipId " + newMembershipId + " is already assigned to another user.");
                    }
                } else {
                    throw new MemberNotFoundException("MembershipId " + newMembershipId + " does not exist.");
                }
            }
        } else {
            throw new UserNotFoundException("User with EMAIL " + email + " not found.");
        }
    }



    @Override
    //@Transactional
    public void updateUserBooking(String email) {
        // Check if the user needs an update (you can add your update conditions here)
        Optional<User> existingUserOptional = userRepository.findByEmail(email);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            List<Booking> bookings = bookingRepository.findByEmail(email);
            int bookingCount = bookings.size();
            existingUser.setBookingCount(bookingCount);

//            existingUser.setAttendedPercentage(getAttendancePercentage(email));
              userRepository.save(existingUser);
        }
        else {
            throw new UserNotFoundException(email);
        }
    }

    @Override
    public int getFutureBookingCount(String email){
        List<Booking> bookings = bookingRepository.findByEmail(email);
        int count = 0;
        LocalDate localDate = LocalDate.now();
        for(Booking b: bookings){
            LocalDate bookingDate = b.getBookingDate().toLocalDate();
            if(bookingDate.isAfter(localDate)){
                count++;
            }
        }
        return count;
    }
    @Override
    public void updateUserPassword(String email, String newPassword){
        Optional<User> u = userRepository.findByEmail(email);
        if (u.isPresent()) {
            User existingUser = u.get();
            existingUser.setHashPassword(encoder.encode(newPassword));
            userRepository.save(existingUser);
        }
        else {
            throw new UserNotFoundException(email);
        }
    }

}
