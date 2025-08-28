package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.dto.CreateAppointmentDto;
import com.healthlinkteam.healthlink.dto.SendNotificationDTO;
import com.healthlinkteam.healthlink.entity.Appointment;
import com.healthlinkteam.healthlink.entity.Notification;
import com.healthlinkteam.healthlink.entity.User;
import com.healthlinkteam.healthlink.enums.NotificationType;
import com.healthlinkteam.healthlink.enums.UserRole;
import com.healthlinkteam.healthlink.repository.AppointmentRepository;
import com.healthlinkteam.healthlink.repository.NotificationRepository;
import com.healthlinkteam.healthlink.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    public void sendNotification(UUID recipientId, String description, NotificationType type) {
        Notification notification = new Notification();
        notification.setRecipientId(recipientId);
        notification.setDescription(description);
        notification.setType(type);
        notificationRepository.save(notification);
    }

    public void sendNotification(SendNotificationDTO sendDTO) {
        Notification notification = new Notification();
        notification.setRecipientId(sendDTO.getRecipientId());
        notification.setSenderId(sendDTO.getSenderId());
        notification.setDescription(sendDTO.getDescription());
        notification.setType(sendDTO.getType());
        notificationRepository.save(notification);
    }

    public void sendNotificationForAppointment(UUID appointmentId, String description) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getDoctor().getId() != null) {
            sendNotification(appointment.getDoctor().getId(), description, NotificationType.GENERAL);
        }

        // Also send to managers
        sendSystemNotification(description, NotificationType.GENERAL);
    }

    public void sendSystemNotification(String description, NotificationType type) {
        List<User> managers = userRepository.findByRole(UserRole.MANAGER);
        for (User manager : managers) {
            sendNotification(manager.getId(), description, type);
        }
    }

    public List<Notification> getUserNotifications(UUID userId) {
        return notificationRepository.findByRecipientId(userId);
    }

    public List<Notification> getUnreadUserNotifications(UUID userId) {
        return notificationRepository.findByRecipientIdAndIsReadFalse(userId);
    }

    public List<Notification> getOverduePaymentNotifications() {
        return notificationRepository.findByType(NotificationType.OVERDUE_PAYMENT);
    }

    public void markAsRead(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    public void createDelayedAppointmentNotifications() {
        List<Appointment> delayedAppointments = appointmentRepository.findDelayedAppointments(LocalDateTime.now());
        for (Appointment appointment : delayedAppointments) {
            sendSystemNotification(
                    "Delayed appointment: Patient " + appointment.getPatient().getId()+
                            " scheduled for " + appointment.getAppointmentDate(),
                    NotificationType.DELAYED_APPOINTMENT
            );
        }
    }
}
