package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Payment entity.
 * Provides data access methods for payment operations.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    /**
     * Find payments by patient ID, ordered by creation date descending
     */
    List<Payment> findByPatientIdOrderByCreatedAtDesc(UUID patientId);

    /**
     * Find payments by appointment ID
     */
    List<Payment> findByAppointmentId(UUID appointmentId);

    /**
     * Find payments by status
     */
    List<Payment> findByStatus(String status);
} 