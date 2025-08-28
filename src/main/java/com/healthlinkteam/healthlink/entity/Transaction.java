package com.healthlinkteam.healthlink.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Transaction entity to record wallet and payment movements.
 */
@Entity
@Table(name = "transactions")
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @ToString.Exclude
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    @ToString.Exclude
    private Payment payment;

    @Column(name = "type", nullable = false)
    private String type; // TOP_UP, PAYMENT, REFUND

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "description")
    private String description;

    @Column(name = "reference")
    private String reference;

    @Column(name = "status", nullable = false)
    private String status; // COMPLETED, FAILED

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}