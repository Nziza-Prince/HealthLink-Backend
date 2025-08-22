package com.healthlinkteam.healthlink.enums;

public enum UserRole {
    PATIENT,    // Patient users - can book appointments, view prescriptions, manage wallet
    DOCTOR,     // Doctor users - can manage patients, write prescriptions, view queue
    MANAGER     // Manager users - can manage staff, view analytics, manage inventory
}
