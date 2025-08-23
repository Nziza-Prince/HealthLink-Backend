package com.healthlinkteam.healthlink.entity;

import com.healthlinkteam.healthlink.dto.CreateAppointmentDto;
import com.healthlinkteam.healthlink.enums.MedicationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "medications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "visit_request_id")
    private CreateAppointmentDto visitRequest;

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
