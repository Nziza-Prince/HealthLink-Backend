package com.healthlinkteam.healthlink.dto;

import lombok.Data;

@Data
public class OverviewStatsDTO {
    private Long totalVisitRequests;
    private Long totalStaff;
    private Long totalMedicalInventory;
    private Long totalFacilities;
    private Double totalRevenue;
    private Double totalExpenses;
    private Double totalProfit;
}