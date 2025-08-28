package com.healthlinkteam.healthlink.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
public class PatientProfileDto {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String gender;
    private String address;
    private String phoneNumber;
    private String countryOfResidence;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Include wallet info without the full wallet object
    private UUID walletId;
    private String walletBalance;
    private String walletCurrency;
} 