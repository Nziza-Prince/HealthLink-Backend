package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.dto.*;
import com.healthlinkteam.healthlink.entity.Patient;
import com.healthlinkteam.healthlink.entity.RefreshToken;
import com.healthlinkteam.healthlink.entity.User;
import com.healthlinkteam.healthlink.exception.BadRequestException;
import com.healthlinkteam.healthlink.repository.RefreshTokenRepository;
import com.healthlinkteam.healthlink.repository.UserRepository;
import com.healthlinkteam.healthlink.security.JwtUtils; // See below
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Configure in SecurityConfig

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${app.jwt.refresh.expiration}")
    private long refreshExpirationMs;

    public User patientSignup(PatientSignup request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use");
        }

        Patient patient = new Patient();
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setNationalId(request.getNationalId());
        patient.setEmail(request.getEmail());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setPhoneNumber(request.getPhoneNumber());
        patient.setAddress(request.getAddress());
        patient.setPassword(passwordEncoder.encode(request.getPassword()));
        patient.setRole("PATIENT");


        return userRepository.save(patient);
    }

    public AuthResponse login(PatientLogin request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtUtils.generateAccessToken(user.getEmail());

        RefreshToken refreshToken = createRefreshToken(user);
        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    public AuthResponse refreshToken(TokenRefresh request) {
        String refreshTokenStr = request.getRefreshToken();
        return refreshTokenRepository.findByToken(refreshTokenStr)
                .map(token -> {
                    if (token.getExpiryDate().isBefore(Instant.now())) {
                        refreshTokenRepository.delete(token);
                        throw new BadRequestException("Refresh token expired");
                    }
                    String newAccessToken = jwtUtils.generateAccessToken(token.getUser().getEmail());
                    return new AuthResponse(newAccessToken, refreshTokenStr);
                })
                .orElseThrow(() -> new BadRequestException("Invalid refresh token"));
    }

    @Transactional
    public void logout(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    private RefreshToken createRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user); // Delete old if exists

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plusMillis(refreshExpirationMs));
        return refreshTokenRepository.save(token);
    }
}