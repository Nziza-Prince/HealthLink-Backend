package com.healthlinkteam.healthlink.dto;

import lombok.Data;

@Data
public class QueueStatsDTO {
    private Long totalVisitRequests;
    private Long availableDoctors;
    private Long totalMedicalInventory;
    private Long totalFacilities;
}