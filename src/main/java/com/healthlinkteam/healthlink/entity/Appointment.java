package com.healthlinkteam.healthlink.entity;

import com.healthlinkteam.healthlink.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointments")
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;

    @Column(name = "duration_minutes")
    private Integer durationMinutes = 30;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @Column(name = "queue_position")
    private Integer queuePosition;

    @Column(name = "amount_paid", precision = 10, scale = 2)
    private BigDecimal amountPaid = BigDecimal.ZERO;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_referred_id")
    private Doctor referedDoctor;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}