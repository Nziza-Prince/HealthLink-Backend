package com.healthlinkteam.healthlink.dto;

import com.healthlinkteam.healthlink.enums.TransactionType;
import lombok.Data;

@Data
public class CreateFinancialRecordDTO {
    private TransactionType type;
    private Double amount;
    private String description;
    private Long visitRequestId;
}