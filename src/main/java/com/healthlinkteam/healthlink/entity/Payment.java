package com.healthlinkteam.healthlink.entity;

import com.healthlinkteam.healthlink.enums.PaymentMethod;
import com.healthlinkteam.healthlink.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "reference_number", unique = true)
    private String referenceNumber;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "paid_date")
    private LocalDateTime paidDate;

    @Column(name = "description")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}