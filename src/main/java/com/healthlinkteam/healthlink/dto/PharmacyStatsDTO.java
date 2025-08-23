package com.healthlinkteam.healthlink.dto;

import lombok.Data;

@Data
public class PharmacyStatsDTO {
    private Long inStock;
    private Long pending;
    private Long lowStockItems;
    private Long collected;
}
