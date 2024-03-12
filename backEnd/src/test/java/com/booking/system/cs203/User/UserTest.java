package com.booking.system.cs203.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.booking.system.cs203.entity.Membership;
import com.booking.system.cs203.entity.User;
import com.booking.system.cs203.entity.Booking;
import com.booking.system.cs203.Booking.BookingRepository;
import com.booking.system.cs203.Membership.MemberService;
import com.booking.system.cs203.Membership.MemberNotFoundException;


import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
public class UserTest {
    @InjectMocks
    private UserServiceImpl userService;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private BookingRepository bookingRepository;
    
    @Mock
    private MemberService membership;
    
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    void getAllUsers_ReturnsListOfUsers() {
        when(userRepository.findAll()).thenReturn(new ArrayList<User>());
        List<User> users = userService.getAllUsers();
        
        assertNotNull(users);
        verify(userRepository).findAll();
    }
    
    @Test
    void createUser_UserDoesNotExist_ReturnsUser() {
        // Successfull creation of user
        User user = new User();
        user.setEmail("test@gmail.com");
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        User result = userService.createUser(user);
        
        assertEquals(user, result);
        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository).save(user);
    }
    
    @Test
    void updateUser_UserExists_ReturnsUpdatedUser() {
        String email = "test@gmail.com";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        User result = userService.updateUser(email, user);
        assertEquals(user, result);
        
        // Verifications
        verify(userRepository).findByEmail(email);
        verify(userRepository).save(user);
    }
    
    @Test
    void updateUserMember_UpdatesMembership() {
        String email = "test@gmail.com";
        User user = new User();
        user.setMembershipId(1L);
        Long newMembershipId = 2L;
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(membership.findMemberById(newMembershipId)).thenReturn(new Membership());
        when(userRepository.existsByMembershipIdAndEmailIsNot(newMembershipId, email)).thenReturn(false);
        
        
        userService.updateUserMember(email, newMembershipId);
        assertEquals(newMembershipId, user.getMembershipId());
        
        // Verifications
        verify(userRepository).findByEmail(email);
        verify(membership).findMemberById(newMembershipId);
        verify(userRepository).existsByMembershipIdAndEmailIsNot(newMembershipId, email);
        verify(userRepository).save(user);
        
    }
    
    @Test
    void findUserByEmail_ExistingEmail_ReturnUser() {
        // arrange
        String email = "existing@gmail.com";
        User user = new User();
        user.setEmail(email);
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        
        // act
        User foundUser = userService.findUserByEmail(email);
        
        // assert
        assertNotNull(foundUser);
        assertEquals(email, foundUser.getEmail());
        verify(userRepository).findByEmail(email);
    }
    
    @Test
    void findUserByEmail_NonExistingEmail_ThrowsException() {
        // arrange
        String email = "non-existing@gmail.com";
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        
        // assert
        try {
            userService.findUserByEmail(email);
        } catch (UserNotFoundException e) {
            assertEquals("Could not find User with email: " + email, e.getMessage());
        }
        
        verify(userRepository).findByEmail(email);
    }
    
    @Test
    void getBookingCount_ValidEmail_ReturnsBookingCount() {
        String email = "test@gmail.com";
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking());
        bookings.add(new Booking());
        
        when(bookingRepository.findByEmail(email)).thenReturn(bookings);
        
        int result = userService.getBookingCount(email);
        
        assertEquals(2, result);
        verify(bookingRepository).findByEmail(email);
    }
    
    @Test
    void getAttendancePercentage_ReturnsCorrectPercentage() {
        // Arrange
        String email = "test@gmail.com";
        List<Booking> bookings = Arrays.asList(new Booking(), new Booking(), new Booking()); // mock 3 bookings
        int totalBookings = bookings.size();
        int attendedBookings = 2;  // mock attended bookings
        double expectedPercentage = (double) attendedBookings / totalBookings;
        
        when(bookingRepository.findByEmail(email)).thenReturn(bookings);  // Corrected this line
        when(bookingRepository.countByEmailAndAttended(email, true)).thenReturn(attendedBookings);
        
        // Act
        double resultPercentage = userService.getAttendancePercentage(email);
        
        // Assert
        assertEquals(expectedPercentage, resultPercentage);
        
        // Verifications
        verify(bookingRepository).findByEmail(email);
        verify(bookingRepository).countByEmailAndAttended(email, true);
    }

}
