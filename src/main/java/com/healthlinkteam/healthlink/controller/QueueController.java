package com.healthlinkteam.healthlink.controller;

import com.healthlinkteam.healthlink.dto.AssignAppointmentDTO;
import com.healthlinkteam.healthlink.dto.CreateAppointmentDto;
import com.healthlinkteam.healthlink.dto.NotificationDTO;
import com.healthlinkteam.healthlink.service.NotificationService;
import com.healthlinkteam.healthlink.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/queue")
@CrossOrigin(origins = "*")
public class QueueController {

    @Autowired
    private QueueService queueService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/unassigned")
    public ResponseEntity<List<CreateAppointmentDto>> getUnassignedQueue() {
        return ResponseEntity.ok(queueService.getUnassignedQueue());
    }

    @GetMapping("/all")
    public ResponseEntity<List<CreateAppointmentDto>> getAllQueue() {
        return ResponseEntity.ok(queueService.getAllQueue());
    }

    @PostMapping("/assign-doctor")
    public ResponseEntity<String> assignDoctor(@RequestBody AssignAppointmentDTO assignDTO) {
        queueService.assignDoctor(assignDTO);
        return ResponseEntity.ok("Doctor assigned successfully");
    }

    @PostMapping("/notify/{visitRequestId}")
    public ResponseEntity<String> sendNotification(@PathVariable UUID visitRequestId,
                                                   @RequestBody NotificationDTO notificationDTO) {
        notificationService.sendNotificationForAppointment(visitRequestId, notificationDTO.getDescription());
        return ResponseEntity.ok("Notification sent successfully");
    }

    @GetMapping("/delayed")
    public ResponseEntity<List<CreateAppointmentDto>> getDelayedAppointments() {
        return ResponseEntity.ok(queueService.getDelayedAppointments());
    }
}
