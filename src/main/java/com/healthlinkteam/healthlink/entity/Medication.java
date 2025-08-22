package com.healthlinkteam.healthlink.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medication {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int stockLevel;
    private String supplier;
    @Enumerated(EnumType.STRING)
    private InventoryStatus status;
    private LocalDate lastRestocked;
}