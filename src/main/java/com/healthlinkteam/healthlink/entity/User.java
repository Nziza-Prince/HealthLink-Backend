package com.healthlinkteam.healthlink.entity;

import com.healthlinkteam.healthlink.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;


@Entity
@Table(name = "users")
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "national_id")
    private String nationalId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    private String gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "country_of_residence")
    private String countryOfResidence;

    private String otp;
    private String address;
    private String password;

    @Transient
    private String confirmPassword;

    private UserRole role;

    @Column(name = "joined_date")
    private LocalDate joinedDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}