package com.healthlinkteam.healthlink.dto;

import com.healthlinkteam.healthlink.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object for user login requests.
 * Contains email, password, and remember me option.
 */
@Data
public class LoginDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private UserRole role;

    private Boolean rememberMe = false;
} 