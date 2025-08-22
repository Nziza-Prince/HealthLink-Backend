package com.healthlinkteam.healthlink.enums;

/**
 * Enumeration for payment statuses in the Health Sync system.
 * Tracks the current state of patient payments.
 */
public enum PaymentStatus {
    PENDING,    // Payment is pending
    COMPLETED,  // Payment completed successfully
    FAILED,     // Payment failed
    OVERDUE,    // Payment is overdue
    CANCELLED   // Payment was cancelled
} 