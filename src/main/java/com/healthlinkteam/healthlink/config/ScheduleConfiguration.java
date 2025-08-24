package com.healthlinkteam.healthlink.config;

import com.healthlinkteam.healthlink.service.InventoryService;
import com.healthlinkteam.healthlink.service.NotificationService;
import com.healthlinkteam.healthlink.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduleConfiguration {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private PaymentService financialService;

    // Check for delayed appointments every hour
    @Scheduled(fixedRate = 3600000) // 1 hour
    public void checkDelayedAppointments() {
        notificationService.createDelayedAppointmentNotifications();
    }

    // Check for low stock every 6 hours
    @Scheduled(fixedRate = 21600000) // 6 hours
    public void checkLowStock() {
        inventoryService.checkLowStockAndNotify();
    }

    // Check for overdue payments daily
    @Scheduled(cron = "0 0 9 * * *") // 9 AM daily
    public void checkOverduePayments() {
        financialService.checkAndNotifyOverduePayments();
    }
}

