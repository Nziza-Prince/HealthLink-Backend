package com.healthlinkteam.healthlink.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
public class PatientSignup {
    private String firstName;
    private String lastName;
    private String nationalId;
    private String email;
    private Date dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String address;
    private String password;
    private String confirmPassword;
}
