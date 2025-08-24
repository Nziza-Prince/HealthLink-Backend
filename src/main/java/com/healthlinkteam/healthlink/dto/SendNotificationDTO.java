package com.healthlinkteam.healthlink.dto;

import com.healthlinkteam.healthlink.enums.NotificationType;
import lombok.Data;

import java.util.UUID;

@Data
public class SendNotificationDTO {
    private UUID recipientId;
    private UUID senderId;
    private String description;
    private NotificationType type;
}
