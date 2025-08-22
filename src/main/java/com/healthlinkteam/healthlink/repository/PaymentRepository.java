package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
    long countByOverdueTrue();
}
