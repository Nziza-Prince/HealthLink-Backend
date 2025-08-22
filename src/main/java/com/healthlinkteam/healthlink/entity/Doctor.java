package com.healthlinkteam.healthlink.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Doctor entity that extends the base User class.
 * Contains doctor-specific information and department assignments.
 */
@Entity
@Table(name = "doctors")
@Data
@EqualsAndHashCode(callSuper = true)
public class Doctor extends User {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "license_number", unique = true)
    private String licenseNumber;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    private String gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    public Doctor() {
        setRole(com.healthlinkteam.healthlink.enums.UserRole.DOCTOR);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
} 