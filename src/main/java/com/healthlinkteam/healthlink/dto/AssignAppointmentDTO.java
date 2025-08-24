package com.healthlinkteam.healthlink.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for assigning an appointment to a doctor.
 */
@Data
public class AssignAppointmentDTO {
    private UUID doctorId;
    private LocalDateTime appointmentDate;
    private String department;
    private String description; // manager-provided description for notification
}
