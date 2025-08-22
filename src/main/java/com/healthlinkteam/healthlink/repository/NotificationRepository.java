package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepository extends JpaRepository<Notification, Long> { }