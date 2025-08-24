package com.healthlinkteam.healthlink.dto;

import com.healthlinkteam.healthlink.enums.UserRole;
import lombok.Data;

@Data
public class CreateUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
    private UserRole role;
    private String department;
    private String passwordHash;
}