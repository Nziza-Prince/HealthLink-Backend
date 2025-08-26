package com.healthlinkteam.healthlink.dto;

import com.healthlinkteam.healthlink.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PaymentDTO {
    private UUID id;
    private UUID patientId;
    private UUID appointmentId;
    private BigDecimal amount;
    private String paymentMethod;
    private PaymentStatus status;
    private String referenceNumber;
    private String phoneNumber;
    private LocalDateTime dueDate;
    private LocalDateTime paidDate;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
