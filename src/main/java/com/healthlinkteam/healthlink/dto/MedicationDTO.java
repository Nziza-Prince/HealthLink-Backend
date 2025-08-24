package com.healthlinkteam.healthlink.dto;

import com.healthlinkteam.healthlink.enums.MedicationStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MedicationDTO {
    private UUID id;
    private UUID appointmentId;
    private String medicationName;
    private String dosage;
    private String duration;
    private String frequency;
    private String specificInstructions;
    private MedicationStatus status;
    private LocalDateTime createdAt;
}
