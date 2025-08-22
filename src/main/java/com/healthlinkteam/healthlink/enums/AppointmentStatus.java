package com.healthlinkteam.healthlink.enums;

public enum AppointmentStatus {
    SCHEDULED,      // Appointment is scheduled but not yet started
    IN_QUEUE,       // Patient is in the queue waiting
    IN_CONSULTATION, // Currently being seen by doctor
    COMPLETED,      // Appointment completed successfully
    CANCELLED,      // Appointment was cancelled
    NO_SHOW,        // Patient didn't show up
    REFERRED        // Patient was referred to another doctor
}
