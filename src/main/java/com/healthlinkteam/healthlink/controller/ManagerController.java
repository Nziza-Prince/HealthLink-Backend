package com.healthlinkteam.healthlink.controller;

import com.healthlinkteam.healthlink.dto.OverviewStatsDTO;
import com.healthlinkteam.healthlink.dto.PharmacyStatsDTO;
import com.healthlinkteam.healthlink.dto.QueueStatsDTO;
import com.healthlinkteam.healthlink.entity.Manager;
import com.healthlinkteam.healthlink.entity.Notification;
import com.healthlinkteam.healthlink.repository.ManagerRepository;
import com.healthlinkteam.healthlink.service.ManagerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/manager")
@CrossOrigin(origins = "*")
@Tag(name = "Managers")
@SecurityRequirement(name = "bearerAuth")
public class ManagerController {

    private final ManagerService managerService;
    private final ManagerRepository managerRepository;

    public ManagerController(ManagerService managerService, ManagerRepository managerRepository) {
        this.managerService = managerService;
        this.managerRepository = managerRepository;
    }

    @GetMapping("/overview")
    public ResponseEntity<?> getOverviewStats(Authentication authentication) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        }
        return ResponseEntity.ok(managerService.getOverviewStats());
    }

    @GetMapping("/queue-stats")
    public ResponseEntity<?> getQueueStats(Authentication authentication) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        }
        return ResponseEntity.ok(managerService.getQueueStats());
    }

    @GetMapping("/pharmacy-stats")
    public ResponseEntity<?> getPharmacyStats(Authentication authentication) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        }
        return ResponseEntity.ok(managerService.getPharmacyStats());
    }

    @GetMapping("/notifications")
    public ResponseEntity<?> getNotifications(Authentication authentication) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        }
        return ResponseEntity.ok(managerService.getRecentNotifications());
    }
}