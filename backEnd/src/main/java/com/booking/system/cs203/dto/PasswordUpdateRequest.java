package com.booking.system.cs203.dto;

import lombok.Getter;
import lombok.Setter;


public class PasswordUpdateRequest {
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String oldPassword;
    @Getter
    @Setter
    private String newPassword;


}
