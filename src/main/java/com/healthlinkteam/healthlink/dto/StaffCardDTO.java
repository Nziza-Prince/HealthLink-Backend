package com.healthlinkteam.healthlink.dto;

import java.time.LocalDate;


public record StaffCardDTO(Long id, String doctorName, String role,
                           LocalDate joinedDate, String email,
                           String department, LocalDate endDate, boolean active) {}