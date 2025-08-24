package com.healthlinkteam.healthlink.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "doctors")
@Data
@EqualsAndHashCode(callSuper = true)
public class Doctor extends User{
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

    @Column(name = "department")
    private String department;

    @Column(name = "hospital")
    private String hospital;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    public Doctor() {
        setRole(com.healthlinkteam.healthlink.enums.UserRole.DOCTOR);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}