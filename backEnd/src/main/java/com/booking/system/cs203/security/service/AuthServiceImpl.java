package com.booking.system.cs203.security.service;


import com.booking.system.cs203.User.UserRepository;
import com.booking.system.cs203.dto.SignupDTO;
import com.booking.system.cs203.dto.UserDto;
import com.booking.system.cs203.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto createUser(SignupDTO signupDTO) {
        User user = new User();
        user.setName(signupDTO.getName());
        user.setEmail(signupDTO.getEmail());
        user.setNumber(signupDTO.getNumber());
        user.setHashPassword("{bcrypt}"+(new BCryptPasswordEncoder().encode(signupDTO.getPassword())));
        user.setRoles("ROLE_USER");
        User createdUser = userRepository.save(user);
        UserDto userDTO = new UserDto();
        userDTO.setEmail(createdUser.getEmail());
        userDTO.setName(createdUser.getName());
        userDTO.setNumber(createdUser.getNumber());
        userDTO.setRoles("ROLE_USER");
        return userDTO;
    }
}
