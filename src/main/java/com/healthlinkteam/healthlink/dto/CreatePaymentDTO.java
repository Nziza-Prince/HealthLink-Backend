package com.healthlinkteam.healthlink.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreatePaymentDTO {
    private UUID patientId;
    private UUID appointmentId;
    private BigDecimal amount;
    private String paymentMethod;
    private String phoneNumber;
    private LocalDateTime dueDate;
    private String description;
}
