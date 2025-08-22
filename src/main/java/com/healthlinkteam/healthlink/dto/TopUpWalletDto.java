package com.healthlinkteam.healthlink.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data Transfer Object for wallet top-up requests.
 * Used for both authenticated and development endpoints.
 */
@Data
public class TopUpWalletDto {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1000.00", message = "Minimum top-up amount is 1000")
    private BigDecimal amount;

    @NotBlank(message = "Payment method is required")
    @Pattern(regexp = "^(MTN_MOBILE_MONEY|AIRTEL_MONEY|CASH|BANK_TRANSFER)$", 
             message = "Invalid payment method")
    private String paymentMethod;

    // Only used for development/testing endpoints
    private String patientId;
} 