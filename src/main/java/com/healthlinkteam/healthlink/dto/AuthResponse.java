package com.healthlinkteam.healthlink.dto;


import com.healthlinkteam.healthlink.entity.RefreshToken;
import lombok.Data;

@Data
public class AuthResponse {


    private String accessToken;
    private String refreshToken;
    private String message;

    public AuthResponse(String accessToken, String refreshToken, String message) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.message = message;
    }
}
