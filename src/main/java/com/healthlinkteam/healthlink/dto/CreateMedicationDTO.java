package com.healthlinkteam.healthlink.dto;

import com.healthlinkteam.healthlink.enums.MedicationStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateMedicationDTO {
    private UUID appointmentId;
    private String medicationName;
    private String dosage;
    private String duration;
    private String frequency;
    private String specificInstructions;
    private MedicationStatus status;
}
