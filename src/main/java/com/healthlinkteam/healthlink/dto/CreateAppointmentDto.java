package com.healthlinkteam.healthlink.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * Data Transfer Object for creating appointments.
 * Used for both authenticated and development endpoints.
 */
@Data
public class CreateAppointmentDto {

    @NotBlank(message = "Department name is required")
    @Pattern(regexp = "^[a-zA-Z\\s\\-]+$", message = "Department name contains invalid characters")
    private String departmentName;

    @NotBlank(message = "Reason is required")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-\\.,!?()]+$", message = "Reason contains invalid characters")
    private String reason;

    @NotBlank(message = "Preferred date is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date must be in YYYY-MM-DD format")
    private String preferredDate;

    // Only used for development/testing endpoints
    private String patientId;
} 