package com.healthlinkteam.healthlink.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Patient entity that extends the base User class.
 * Contains patient-specific information and relationships.
 */
@Entity
@Table(name = "patients")
@Data
@EqualsAndHashCode(callSuper = true)
public class Patient extends User {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "address")
    private String address;

    @Column(name = "emergency_contact_name")
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone")
    private String emergencyContactPhone;

    @Column(name = "wallet_id")
    private UUID walletId;

    public Patient() {
        setRole(com.healthlinkteam.healthlink.enums.UserRole.PATIENT);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}