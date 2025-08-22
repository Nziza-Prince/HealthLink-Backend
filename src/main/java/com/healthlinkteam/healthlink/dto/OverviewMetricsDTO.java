package com.healthlinkteam.healthlink.dto;

public record OverviewMetricsDTO(long totalVisitRequests,
                                 long totalStaff,
                                 long totalMedicalInventory,
                                 long totalFacilities) { }
