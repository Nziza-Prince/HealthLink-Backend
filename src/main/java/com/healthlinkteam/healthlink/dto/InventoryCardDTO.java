package com.healthlinkteam.healthlink.dto;

import java.time.LocalDate;


public record InventoryCardDTO(Long id, String medicationName, int stockLevel,
                               String supplier, String status, LocalDate lastRestocked) {}