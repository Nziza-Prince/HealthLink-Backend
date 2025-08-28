package com.healthlinkteam.healthlink.entity;

import com.healthlinkteam.healthlink.enums.StockStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "medical_inventory")
@Setter
@Getter
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

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

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
