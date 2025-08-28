package com.healthlinkteam.healthlink.controller;

import com.healthlinkteam.healthlink.dto.SendNotificationDTO;
import com.healthlinkteam.healthlink.entity.Notification;
import com.healthlinkteam.healthlink.repository.ManagerRepository;
import com.healthlinkteam.healthlink.service.NotificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Notifications")
@SecurityRequirement(name = "bearerAuth")
public class NotificationsController {

    private final NotificationService notificationService;
    private final ManagerRepository managerRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable UUID userId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable UUID userId) {
        return ResponseEntity.ok(notificationService.getUnreadUserNotifications(userId));
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody SendNotificationDTO sendDTO) {
        notificationService.sendNotification(sendDTO);
        return ResponseEntity.ok("Notification sent successfully");
    }

    @PostMapping("/{id}/mark-read")
    public ResponseEntity<String> markAsRead(@PathVariable UUID id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok("Notification marked as read");
    }

    @GetMapping("/overdue-payments")
    public ResponseEntity<List<Notification>> getOverduePaymentNotifications() {
        return ResponseEntity.ok(notificationService.getOverduePaymentNotifications());
    }
}
