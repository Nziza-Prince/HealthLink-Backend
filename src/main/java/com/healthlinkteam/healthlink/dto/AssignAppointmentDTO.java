package com.healthlinkteam.healthlink.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for assigning an appointment to a doctor.
 */
@Data
public class AssignAppointmentDTO {
    private UUID appointmentId;   // <-- add this
    private UUID doctorId;
    private String department;
    private String description;
    private LocalDateTime appointmentDate;
}
