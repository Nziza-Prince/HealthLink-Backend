package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.MedicalInventory;
import com.healthlinkteam.healthlink.enums.StockStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalInventoryRepository extends JpaRepository<MedicalInventory, Long> {
    List<MedicalInventory> findByStatus(StockStatus status);
    List<MedicalInventory> findByMedicineNameContainingIgnoreCase(String name);

    @Query("SELECT COUNT(m) FROM MedicalInventory m WHERE m.status = 'IN_STOCK'")
    Long countInStock();

    @Query("SELECT COUNT(m) FROM MedicalInventory m WHERE m.status = 'LOW_STOCK'")
    Long countLowStock();

    @Query("SELECT COUNT(m) FROM MedicalInventory m WHERE m.status = 'PENDING'")
    Long countPending();
}