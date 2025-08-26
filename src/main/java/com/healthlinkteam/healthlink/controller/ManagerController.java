package com.healthlinkteam.healthlink.controller;

import com.healthlinkteam.healthlink.dto.OverviewStatsDTO;
import com.healthlinkteam.healthlink.dto.PharmacyStatsDTO;
import com.healthlinkteam.healthlink.dto.QueueStatsDTO;
import com.healthlinkteam.healthlink.entity.Notification;
import com.healthlinkteam.healthlink.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@CrossOrigin(origins = "*")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @GetMapping("/overview")
    public ResponseEntity<OverviewStatsDTO> getOverviewStats() {
        return ResponseEntity.ok(managerService.getOverviewStats());
    }

    @GetMapping("/queue-stats")
    public ResponseEntity<QueueStatsDTO> getQueueStats() {
        return ResponseEntity.ok(managerService.getQueueStats());
    }

    @GetMapping("/pharmacy-stats")
    public ResponseEntity<PharmacyStatsDTO> getPharmacyStats() {
        return ResponseEntity.ok(managerService.getPharmacyStats());
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> getNotifications() {
        return ResponseEntity.ok(managerService.getRecentNotifications());
    }
}