package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.dto.CreateAppointmentDto;
import com.healthlinkteam.healthlink.entity.Medication;
import com.healthlinkteam.healthlink.enums.MedicationStatus;
import com.healthlinkteam.healthlink.enums.StockStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
    List<Medication> findByStatus(MedicationStatus status);
    List<Medication> findByVisitRequest(CreateAppointmentDto visitRequest);

    @Query("SELECT COUNT(m) FROM Medication m WHERE m.status = 'COLLECTED'")
    Long countCollected();
}
