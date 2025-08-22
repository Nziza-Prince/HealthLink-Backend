package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.dto.RegisterDoctor;
import com.healthlinkteam.healthlink.entity.Doctor;
import com.healthlinkteam.healthlink.repository.DoctorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> signup(RegisterDoctor signupDto) {
        try {
            // Check if passwords match
            if (!signupDto.getPassword().equals(signupDto.getConfirmPassword())) {
                return ResponseEntity.badRequest().body("Passwords do not match");
            }

            // Check if email already exists
            if (doctorRepository.existsByEmail(signupDto.getEmail())) {
                return ResponseEntity.badRequest().body("Email already registered");
            }

            // Create new patient
            Doctor doctor = new Doctor();
            doctor.setFirstName(signupDto.getFirstName());
            doctor.setLastName(signupDto.getLastName());
            doctor.setEmail(signupDto.getEmail());
            doctor.setCountryOfResidence(signupDto.getCountryResidence());
            doctor.setPhoneNumber(signupDto.getPhoneNumber());
            doctor.setPasswordHash(passwordEncoder.encode(signupDto.getPassword()));

            // Save doctor (this will save both User and Patient records due to JOINED inheritance)
            Doctor savedDoctor = doctorRepository.save(doctor);

            log.info("Doctor registered successfully: {}", savedDoctor.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body("Doctor registered successfully");

        } catch (Exception e) {
            log.error("Error during doctor signup: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
        }
    }
}
