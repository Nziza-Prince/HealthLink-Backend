package com.healthlinkteam.healthlink.entity;

import com.healthlinkteam.healthlink.enums.StockStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "medical_inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String medicineName;

    private String category;

    private Integer stockQuantity;

    @Column(name = "stock_expiry_date")
    private LocalDate stockExpiryDate;

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
        updatedAt = LocalDateTime.now();
        updateStockStatus();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        updateStockStatus();
    }

    public void updateStockStatus() {
        if (stockQuantity == null || stockQuantity == 0) {
            status = StockStatus.OUT_OF_STOCK;
        } else if (stockQuantity < 10) {
            status = StockStatus.LOW_STOCK;
        } else {
            status = StockStatus.IN_STOCK;
        }
    }
}
