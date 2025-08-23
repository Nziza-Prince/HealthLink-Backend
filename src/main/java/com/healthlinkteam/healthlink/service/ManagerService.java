package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.dto.OverviewStatsDTO;
import com.healthlinkteam.healthlink.dto.PharmacyStatsDTO;
import com.healthlinkteam.healthlink.dto.QueueStatsDTO;
import com.healthlinkteam.healthlink.entity.Notification;
import com.healthlinkteam.healthlink.enums.UserRole;
import com.healthlinkteam.healthlink.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointmentRepository visitRequestRepository;

    @Autowired
    private MedicalInventoryRepository inventoryRepository;

    @Autowired
    private PaymentRepository financialRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    // Overview Statistics
    public OverviewStatsDTO getOverviewStats() {
        OverviewStatsDTO stats = new OverviewStatsDTO();
        stats.setTotalVisitRequests(visitRequestRepository.countTotalVisitRequests());
        stats.setTotalStaff((long) userRepository.findByIsActiveTrue().size());
        stats.setTotalMedicalInventory((long) inventoryRepository.findAll().size());
        stats.setTotalFacilities(10L); // Assuming static value

        Double revenue = financialRepository.getTotalRevenue();
        Double expenses = financialRepository.getTotalExpenses();
        stats.setTotalRevenue(revenue != null ? revenue : 0.0);
        stats.setTotalExpenses(expenses != null ? expenses : 0.0);
        stats.setTotalProfit((stats.getTotalRevenue() - stats.getTotalExpenses()));

        return stats;
    }

    // Queue Statistics
    public QueueStatsDTO getQueueStats() {
        QueueStatsDTO stats = new QueueStatsDTO();
        stats.setTotalVisitRequests(visitRequestRepository.countTotalVisitRequests());
        stats.setAvailableDoctors((long) userRepository.findByRoleAndIsActiveTrue(UserRole.DOCTOR).size());
        stats.setTotalMedicalInventory((long) inventoryRepository.findAll().size());
        stats.setTotalFacilities(10L);
        return stats;
    }

    // Pharmacy Statistics
    public PharmacyStatsDTO getPharmacyStats() {
        PharmacyStatsDTO stats = new PharmacyStatsDTO();
        stats.setInStock(inventoryRepository.countInStock());
        stats.setPending(inventoryRepository.countPending());
        stats.setLowStockItems(inventoryRepository.countLowStock());
        stats.setCollected(medicationRepository.countCollected());
        return stats;
    }

    // Get all notifications for dashboard
    public List<Notification> getRecentNotifications() {
        return notificationRepository.findAll().stream()
                .sorted((n1, n2) -> n2.getCreatedAt().compareTo(n1.getCreatedAt()))
                .limit(10)
                .collect(Collectors.toList());
    }
}