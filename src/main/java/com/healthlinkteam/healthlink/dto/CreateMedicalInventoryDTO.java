package com.healthlinkteam.healthlink.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateMedicalInventoryDTO {
    private String medicineName;
    private String category;
    private Integer stockQuantity;
    private LocalDate stockExpiryDate;
    private String supplierName;
    private String supplierContact;
    private String storageInstructions;
}
