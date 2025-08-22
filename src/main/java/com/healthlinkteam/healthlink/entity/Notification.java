package com.healthlinkteam.healthlink.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category; // Transactions | Delayed appointments | Overdue payments | etc
    private String message;
    private boolean readFlag;
    private LocalDateTime createdAt;
}