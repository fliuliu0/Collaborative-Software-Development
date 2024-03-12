package com.booking.system.cs203.security.service;

import com.booking.system.cs203.dto.SignupDTO;
import com.booking.system.cs203.dto.UserDto;

public interface AuthService {
    UserDto createUser(SignupDTO signupDTO);
}
