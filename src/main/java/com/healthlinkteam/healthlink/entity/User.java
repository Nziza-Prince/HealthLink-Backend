package com.healthlinkteam.healthlink.entity;

import com.healthlinkteam.healthlink.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;


@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    private String gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String otp;

    private String address;

    @Column(name = "country_of_residence")
    private String countryOfResidence;

    @Column(name = "department")
    private String department;

    @Transient
    private String confirmPassword;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}