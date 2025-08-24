package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Notification;
import com.healthlinkteam.healthlink.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByRecipientIdAndIsReadFalse(UUID recipientId);
    List<Notification> findByRecipientId(UUID recipientId);
    List<Notification> findByType(NotificationType type);
}
