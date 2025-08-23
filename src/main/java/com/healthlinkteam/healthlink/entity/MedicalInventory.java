package com.healthlinkteam.healthlink.entity;

import com.healthlinkteam.healthlink.enums.StockStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "medical_inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String medicineName;

    private String category;
    private Integer stockQuantity;
    private LocalDateTime stockExpiryDate;
    private String supplierName;
    private String supplierContact;
    private String storageInstructions;

    @Enumerated(EnumType.STRING)
    private StockStatus status;

    private LocalDateTime lastRestocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updateStockStatus();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        updateStockStatus();
    }

    private void updateStockStatus() {
        if (stockQuantity == null || stockQuantity == 0) {
            status = StockStatus.OUT_OF_STOCK;
        } else if (stockQuantity < 10) {
            status = StockStatus.LOW_STOCK;
        } else {
            status = StockStatus.IN_STOCK;
        }
    }
}