package com.healthlinkteam.healthlink.controller;

import com.healthlinkteam.healthlink.dto.AssignAppointmentDTO;
import com.healthlinkteam.healthlink.dto.CreateAppointmentDto;
import com.healthlinkteam.healthlink.dto.NotificationDTO;
import com.healthlinkteam.healthlink.entity.Manager;
import com.healthlinkteam.healthlink.enums.AppointmentStatus;
import com.healthlinkteam.healthlink.repository.ManagerRepository;
import com.healthlinkteam.healthlink.service.NotificationService;
import com.healthlinkteam.healthlink.service.QueueService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/queue")
@CrossOrigin(origins = "*")
@Tag(name = "Queue")
@SecurityRequirement(name = "bearerAuth")
public class QueueController {
    private final QueueService queueService;
    private final NotificationService notificationService;
    private final ManagerRepository managerRepository;

    public QueueController(QueueService queueService, NotificationService notificationService, ManagerRepository managerRepository) {
        this.queueService = queueService;
        this.notificationService = notificationService;
        this.managerRepository = managerRepository;
    }

    @GetMapping("/unassigned")
    public ResponseEntity<?> getUnassignedQueue(
            Authentication authentication
    ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        }
        return ResponseEntity.ok(queueService.getUnassignedQueue());
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllQueue(
            Authentication authentication
    ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        }
        return ResponseEntity.ok(queueService.getAllQueue());
    }

    @PostMapping("/assign-doctor")
    public ResponseEntity<String> assignDoctor(
            @RequestBody AssignAppointmentDTO assignDTO,
            Authentication authentication
            ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        }
        queueService.assignDoctor(assignDTO);
        return ResponseEntity.ok("Doctor assigned successfully");
    }

    @PostMapping("/notify/{visitRequestId}")
    public ResponseEntity<String> sendNotification(
            @PathVariable UUID visitRequestId,
                                                   @RequestBody NotificationDTO notificationDTO,
            Authentication authentication
            ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        }
        notificationService.sendNotificationForAppointment(visitRequestId, notificationDTO.getDescription());
        return ResponseEntity.ok("Notification sent successfully");
    }

    @GetMapping("/delayed")
    public ResponseEntity<?> getDelayedAppointments(
            Authentication authentication
    ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        }
        return ResponseEntity.ok(queueService.getDelayedAppointments());
    }
}
