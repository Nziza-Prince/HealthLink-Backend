package com.healthlinkteam.healthlink.dto;

import com.healthlinkteam.healthlink.enums.UserRole;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UserDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
    private UserRole role;
    private String department;
    private LocalDate joinedDate;
    private LocalDate endDate;
    private Boolean isActive;
}
