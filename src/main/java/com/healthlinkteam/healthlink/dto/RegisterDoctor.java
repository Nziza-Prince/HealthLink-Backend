package com.healthlinkteam.healthlink.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterDoctor {

    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    @NotBlank(message = "Email is required.")
    private String email;

    @NotBlank(message = "Country is required.")
    private String countryResidence;

    @NotBlank(message = "Phone is required.")
    private String phoneNumber;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "Confirm Password is required.")
    private String confirmPassword;

}
