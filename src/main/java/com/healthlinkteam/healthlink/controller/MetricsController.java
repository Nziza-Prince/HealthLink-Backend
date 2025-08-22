package com.healthlinkteam.healthlink.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class MetricsController {
    private final MetricsService metrics;


    @GetMapping("/overview")
    public OverviewMetricsDTO overview() {
        return new OverviewMetricsDTO(
                metrics.totalVisitRequests(),
                metrics.totalStaff(),
                metrics.totalMedicalInventory(),
                metrics.totalFacilities()
        );
    }
}
