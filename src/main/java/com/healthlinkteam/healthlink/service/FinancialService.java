package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.repository.PaymentRepository;
import com.healthlinkteam.healthlink.repository.TransactionRepository;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class FinancialService {
    private final TransactionRepository txRepo;
    private final PaymentRepository paymentRepo;


    public BigDecimal totalRevenue() {
        return txRepo.findAll().stream()
                .filter(t -> "REVENUE".equalsIgnoreCase(t.getType()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public BigDecimal totalExpenses() {
        return txRepo.findAll().stream()
                .filter(t -> "EXPENSE".equalsIgnoreCase(t.getType()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public BigDecimal totalProfit() {
        return totalRevenue().subtract(totalExpenses());
    }


    public long overduePaymentsCount() { return paymentRepo.countByOverdueTrue(); }
}