package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.dto.OverviewStatsDTO;
import com.healthlinkteam.healthlink.dto.PharmacyStatsDTO;
import com.healthlinkteam.healthlink.dto.QueueStatsDTO;
import com.healthlinkteam.healthlink.entity.Notification;
import com.healthlinkteam.healthlink.enums.UserRole;
import com.healthlinkteam.healthlink.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerService {
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicalInventoryRepository inventoryRepository;
    private final PaymentRepository paymentRepository;
    private final NotificationRepository notificationRepository;
    private final MedicationRepository medicationRepository;

    public ManagerService(UserRepository userRepository, AppointmentRepository appointmentRepository, MedicalInventoryRepository inventoryRepository, PaymentRepository paymentRepository, NotificationRepository notificationRepository, MedicationRepository medicationRepository) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.inventoryRepository = inventoryRepository;
        this.paymentRepository = paymentRepository;
        this.notificationRepository = notificationRepository;
        this.medicationRepository = medicationRepository;
    }

    // Overview Statistics
    public OverviewStatsDTO getOverviewStats() {
        OverviewStatsDTO stats = new OverviewStatsDTO();
        stats.setTotalVisitRequests(appointmentRepository.countTotalAppointments()); // ✅ fixed
        stats.setTotalStaff((long) userRepository.findByIsActiveTrue().size());
        stats.setTotalMedicalInventory((long) inventoryRepository.findAll().size());
        stats.setTotalFacilities(10L); // static example

        Double revenue = paymentRepository.getTotalRevenue();
        Double expenses = paymentRepository.getTotalExpenses();
        stats.setTotalRevenue(revenue != null ? revenue : 0.0);
        stats.setTotalExpenses(expenses != null ? expenses : 0.0);
        stats.setTotalProfit(stats.getTotalRevenue() - stats.getTotalExpenses());

        return stats;
    }

    // Queue Statistics
    public QueueStatsDTO getQueueStats() {
        QueueStatsDTO stats = new QueueStatsDTO();
        stats.setTotalVisitRequests(appointmentRepository.countTotalAppointments()); // ✅ fixed
        stats.setAvailableDoctors((long) userRepository.findByRoleAndIsActiveTrue(UserRole.DOCTOR, true).size());
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
