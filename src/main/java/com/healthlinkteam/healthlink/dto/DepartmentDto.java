package com.healthlinkteam.healthlink.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for department information.
 * Used for frontend dropdowns and department selection.
 */
@Data
public class DepartmentDto {

    private UUID id;
    private String name;
    private String description;
    private String hospitalName;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 