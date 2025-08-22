package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Medication;
import com.healthlinkteam.healthlink.enums.InventoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MedicationRepository extends JpaRepository<Medication, Long> {
    long countByStatus(InventoryStatus status);
}