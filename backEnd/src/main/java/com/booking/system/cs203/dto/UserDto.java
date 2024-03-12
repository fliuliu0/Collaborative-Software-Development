package com.booking.system.cs203.dto;

import lombok.Getter;
import lombok.Setter;

public class UserDto {
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String number;
    @Getter
    @Setter
    private String hashPassword;
    @Getter
    @Setter
    private String roles;

}
