package com.healthlinkteam.healthlink.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Object for patient overview/dashboard.
 * Contains all the information displayed on the patient overview page.
 */
@Data
public class PatientOverviewDto {

    // Queue Status Card
    private QueueStatusDto queueStatus;

    // Assigned Doctor Card
    private AssignedDoctorDto assignedDoctor;

    // Total Visits Card
    private TotalVisitsDto totalVisits;

    // Wallet Balance Card
    private BigDecimal walletBalance;

    // Recent Visits Section
    private List<RecentVisitDto> recentVisits;

    // Upcoming Appointments Section
    private List<UpcomingAppointmentDto> upcomingAppointments;

    // Notifications
    private List<NotificationDto> notifications;

    @Data
    public static class QueueStatusDto {
        private String status; // "In Queue", "Waiting", "Completed"
        private String department;
        private Integer position;
        private String estimatedWaitTime;
    }

    @Data
    public static class AssignedDoctorDto {
        private String doctorName;
        private String department;
        private String hospitalName;
        private String specialization;
    }

    @Data
    public static class TotalVisitsDto {
        private Integer totalVisits;
        private String department;
        private Integer thisMonth;
        private Integer thisYear;
    }

    @Data
    public static class RecentVisitDto {
        private String hospitalName;
        private String department;
        private String doctorName;
        private String expectedMeetingDate;
        private String status;
    }

    @Data
    public static class UpcomingAppointmentDto {
        private String hospitalName;
        private String department;
        private String doctorName;
        private String appointmentDate;
        private String appointmentTime;
    }

    @Data
    public static class NotificationDto {
        private String type; // "APPOINTMENT_REMINDER", "RESCHEDULED", "NEW_TRANSACTION"
        private String message;
        private String timestamp;
        private Boolean isRead;
    }
} 