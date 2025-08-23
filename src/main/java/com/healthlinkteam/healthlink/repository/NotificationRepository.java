package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Notification;
import com.healthlinkteam.healthlink.entity.User;
import com.healthlinkteam.healthlink.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientAndIsReadFalse(User recipient);
    List<Notification> findByRecipient(User recipient);
    List<Notification> findByType(NotificationType type);
}