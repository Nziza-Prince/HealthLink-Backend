package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByPatientIdOrderByCreatedAtDesc(UUID patientId);
}