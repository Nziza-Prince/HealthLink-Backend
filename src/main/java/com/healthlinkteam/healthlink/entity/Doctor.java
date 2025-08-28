package com.healthlinkteam.healthlink.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "doctors")
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class Doctor extends User{

    @Column(name = "license_number", unique = true)
    private String licenseNumber;

    @Column(name = "specialization")
    private String specialization;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    public Doctor() {
        setRole(com.healthlinkteam.healthlink.enums.UserRole.DOCTOR);
    }
}