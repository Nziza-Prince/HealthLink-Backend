package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Medication;
import com.healthlinkteam.healthlink.enums.MedicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, UUID> {
    List<Medication> findByStatus(MedicationStatus status);

    @Query("SELECT COUNT(m) FROM Medication m WHERE m.status = 'COLLECTED'")
    Long countCollected();

    List<Medication> findByAppointmentId(UUID appointmentId);
}
