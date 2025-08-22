package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Prescription entity.
 * Provides data access methods for prescription operations.
 */
@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {

    /**
     * Find prescriptions by patient ID, ordered by start date descending
     */
    List<Prescription> findByPatientIdOrderByStartDateDesc(UUID patientId);

    /**
     * Find prescriptions by doctor ID
     */
    List<Prescription> findByDoctorId(UUID doctorId);

    /**
     * Find prescriptions by appointment ID
     */
    List<Prescription> findByAppointmentId(UUID appointmentId);
} 