package com.healthlinkteam.healthlink.entity;

import com.healthlinkteam.healthlink.enums.MedicationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "medications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "appointment_id", nullable = false)
    private UUID appointmentId;

    @Column(nullable = false)
    private String medicationName;

    private String dosage;
    private String duration;
    private String frequency;
    private String specificInstructions;

    @Enumerated(EnumType.STRING)
    private MedicationStatus status = MedicationStatus.PRESCRIBED;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
