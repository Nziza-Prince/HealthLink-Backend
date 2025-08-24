package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Payment;
import com.healthlinkteam.healthlink.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    // Find payments by status
    List<Payment> findByStatus(PaymentStatus status);

    // Overdue payments (assuming status = PENDING and dueDate < now)
    @Query("SELECT p FROM Payment p WHERE p.status = 'PENDING' AND p.dueDate < CURRENT_TIMESTAMP")
    List<Payment> findOverduePayments();

    // Total revenue (if revenue means payments with positive amounts)
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.amount > 0")
    Double getTotalRevenue();

    // Total expenses (if negative amounts)
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.amount < 0")
    Double getTotalExpenses();
}
