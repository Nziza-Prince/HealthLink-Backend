package com.healthlinkteam.healthlink.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;
    private String lastName;

    private String nationalId;

    @Column(unique = true,nullable = false)
    private String email;

    private Date dateOfBirth;

    private String gender;
    private String phoneNumber;
    private String otp;

    private String address;
    private String password;
    private String confirmPassword;
    private String role;

}
