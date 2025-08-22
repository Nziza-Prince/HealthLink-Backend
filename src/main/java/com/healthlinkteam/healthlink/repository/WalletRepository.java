package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Wallet entity.
 * Provides data access methods for wallet operations.
 */
@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    /**
     * Find wallet by patient ID
     */
    Optional<Wallet> findByPatientId(UUID patientId);
} 