package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Payment;
import com.healthlinkteam.healthlink.enums.PaymentStatus;
import com.healthlinkteam.healthlink.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByType(TransactionType type);
    List<Payment> findByPaymentStatus(PaymentStatus status);

    @Query("SELECT SUM(f.amount) FROM Payment f WHERE f.type = 'REVENUE'")
    Double getTotalRevenue();

    @Query("SELECT SUM(f.amount) FROM Payment f WHERE f.type = 'EXPENSE'")
    Double getTotalExpenses();

    @Query("SELECT f FROM Payment f WHERE f.paymentStatus = 'OVERDUE'")
    List<Payment> findOverduePayments();
}
