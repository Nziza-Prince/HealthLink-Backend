package com.healthlinkteam.healthlink.dto;

import com.healthlinkteam.healthlink.enums.AppointmentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for sending appointment data to frontend.
 */
@Data
public class AppointmentDTO {
    private UUID id;
    private UUID patientId;
    private UUID doctorId;
    private String department;
    private String hospital;
    private String reason;
    private String diagnosis;
    private LocalDateTime appointmentDate;
    private Integer durationMinutes;
    private AppointmentStatus status;
    private Integer queuePosition;
    private BigDecimal amountPaid;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
