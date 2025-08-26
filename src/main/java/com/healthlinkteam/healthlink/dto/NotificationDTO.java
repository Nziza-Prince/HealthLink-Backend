package com.healthlinkteam.healthlink.dto;

import com.healthlinkteam.healthlink.enums.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NotificationDTO {
    private UUID id;
    private UUID recipientId;
    private UUID senderId;
    private NotificationType type;
    private String description;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
