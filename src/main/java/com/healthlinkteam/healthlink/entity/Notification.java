package com.healthlinkteam.healthlink.entity;

import com.healthlinkteam.healthlink.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "recipient_id")
    private UUID recipientId;

    @Column(name = "sender_id")
    private UUID senderId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String description;
    private Boolean isRead = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}