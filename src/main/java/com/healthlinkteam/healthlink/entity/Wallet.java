package com.healthlinkteam.healthlink.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "wallets")
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @ToString.Exclude
    private Patient patient;

    @Column(name = "balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "currency", nullable = false)
    private String currency = "USD";

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void addFunds(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public boolean deductFunds(BigDecimal amount) {
        if (this.balance.compareTo(amount) >= 0) {
            this.balance = this.balance.subtract(amount);
            return true;
        }
        return false;
    }
}
