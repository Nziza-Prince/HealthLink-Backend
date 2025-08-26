package com.healthlinkteam.healthlink.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "national_id")
    private String nationalId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    private String gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String otp;
    private String address;
    private String password;

    @Transient
    private String confirmPassword;

    private String role;

}
