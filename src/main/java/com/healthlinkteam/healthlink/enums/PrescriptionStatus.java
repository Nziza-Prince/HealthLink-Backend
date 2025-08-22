package com.healthlinkteam.healthlink.enums;

/**
 * Enumeration for prescription statuses in the Health Sync system.
 * Tracks the current state of patient prescriptions.
 */
public enum PrescriptionStatus {
    ACTIVE,      // Prescription is currently active and being taken
    COMPLETED,   // Prescription course has been completed
    EXPIRED,     // Prescription has expired
    DISCONTINUED // Prescription was discontinued by doctor
} 