package com.healthlinkteam.healthlink.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class WalletDto {
    private UUID id;
    private BigDecimal balance;
    private String currency;
    private LocalDateTime updatedAt;
    private List<WalletTransactionDto> recentTransactions;

    @Data
    public static class WalletTransactionDto {
        private UUID id;
        private String type; // TOP_UP, PAYMENT, REFUND
        private BigDecimal amount;
        private String description;
        private String reference;
        private String status; // COMPLETED, FAILED
        private LocalDateTime createdAt;
    }
}