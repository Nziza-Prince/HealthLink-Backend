package com.healthlinkteam.healthlink.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MedicalInventoryDTO {
    private Long id;
    private String medicineName;
    private String category;
    private Integer stockQuantity;
    private LocalDate stockExpiryDate;
    private String supplierName;
    private String supplierContact;
    private String storageInstructions;
    private String status;
    private LocalDateTime lastRestocked;
}

