package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.dto.*;
import com.healthlinkteam.healthlink.entity.*;
import com.healthlinkteam.healthlink.enums.AppointmentStatus;
import com.healthlinkteam.healthlink.enums.UserRole;
import com.healthlinkteam.healthlink.repository.*;
import com.healthlinkteam.healthlink.security.JwtUtils; // See below
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.RollbackOn;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder; // Configure in SecurityConfig
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final HospitalRepository hospitalRepository;
    private final DepartmentRepository  departmentRepository;

    @Value("${app.jwt.refresh.expiration}")
    private long refreshExpirationMs;

    public AuthService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, AuthenticationManager authenticationManager, DoctorRepository doctorRepository, HospitalRepository hospitalRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.hospitalRepository = hospitalRepository;
        this.departmentRepository = departmentRepository;
    }

    public AuthResponse patientSignup(PatientSignupDto request) {
        try {

            if (!request.getPassword().equals(request.getConfirmPassword())) {
                return new AuthResponse(null, null, "Passwords do not match");
            }
            if (userRepository.existsByEmail(request.getEmail())) {
                return new AuthResponse(null, null, "Email already exists");
            }

            Patient patient = new Patient();
            patient.setFirstName(request.getFirstName());
            patient.setLastName(request.getLastName());
            patient.setNationalId(request.getNationalId());
            patient.setEmail(request.getEmail());
            patient.setDateOfBirth(request.getDateOfBirth());
            patient.setGender(request.getGender());
            patient.setPhoneNumber(request.getPhoneNumber());
            patient.setAddress(request.getCountryOfResidence());
            patient.setPassword(passwordEncoder.encode(request.getPassword()));
            patient.setRole(UserRole.PATIENT);

            userRepository.save(patient);
            return new AuthResponse(null, null, "Created Successfully");
        } catch (Exception ex) {
            return new AuthResponse(null, null, "Internal Server Error");
        }
    }

    public AuthResponse doctorSignup(DoctorSignupDto signupDto) {
        try {
            // Check if passwords match
            if (!signupDto.getPassword().equals(signupDto.getConfirmPassword())) {
                return new AuthResponse(null, null, "Passwords do not match");
            }

            // Check if email already exists
            if (userRepository.existsByEmail(signupDto.getEmail())) {
                return new AuthResponse(null, null, "Email already exists");
            }

            // Create new doctor
            Doctor doctor = new Doctor();
            doctor.setFirstName(signupDto.getFirstName());
            doctor.setLastName(signupDto.getLastName());
            doctor.setNationalId(signupDto.getNationalId());
            doctor.setEmail(signupDto.getEmail());
            doctor.setDateOfBirth(signupDto.getDateOfBirth());
            doctor.setGender(signupDto.getGender());
            doctor.setPhoneNumber(signupDto.getPhoneNumber());
            doctor.setAddress(signupDto.getCountryOfResidence());
            doctor.setPassword(passwordEncoder.encode(signupDto.getPassword()));
            doctor.setRole(UserRole.DOCTOR);
            doctor.setLicenseNumber(signupDto.getLicenseNumber());
            doctor.setSpecialization(signupDto.getSpecialization());
            Department department = departmentRepository.findDepartmentByName(signupDto.getDepartmentName());
            doctor.setDepartment(department);
            Hospital hospital = hospitalRepository.findByName(signupDto.getHospitalName());

            doctor.setHospital(hospital);

            // Save doctor (this will save both User and Doctor records due to JOINED inheritance)
            userRepository.save(doctor);
            return new AuthResponse(null, null, "Created Successfully");

        } catch (Exception e) {
            return new AuthResponse(null, null, "Internal Server Error");
        }
    }

    public AuthResponse managerSignup(ManagerSignupDto signupDto) {
        try {
            // Check if passwords match
            if (!signupDto.getPassword().equals(signupDto.getConfirmPassword())) {
                return new AuthResponse(null, null, "Passwords do not match");
            }

            // Check if email already exists
            if (userRepository.existsByEmail(signupDto.getEmail())) {
                return new AuthResponse(null, null, "Email already exists");
            }

            // Create new patient
            Manager manager = new Manager();
            manager.setFirstName(signupDto.getFirstName());
            manager.setLastName(signupDto.getLastName());
            manager.setEmail(signupDto.getEmail());
            manager.setCountryOfResidence(signupDto.getCountryOfResidence());
            manager.setPhoneNumber(signupDto.getPhoneNumber());
            manager.setDateOfBirth(signupDto.getDateOfBirth());
            manager.setGender(signupDto.getGender());
            manager.setRole(UserRole.MANAGER);

            Hospital hospital = hospitalRepository.findByName(signupDto.getHospitalName());

            manager.setHospital(hospital);
            manager.setPassword(passwordEncoder.encode(signupDto.getPassword()));

            // Save manager (this will save both User and Patient records due to JOINED inheritance)
            userRepository.save(manager);
            return new AuthResponse(null, null, "Created Successfully");

        } catch (Exception e) {
            return new AuthResponse(null, null, "Internal Server Error");
        }
    }

    public AuthResponse login(LoginDto request) {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            // Get the email from the authenticated principal and fetch the actual User entity
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found after authentication"));

            // Verify password
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return new AuthResponse(null, null, "Invalid password");
            }

            String accessToken = jwtUtils.generateAccessToken(user.getEmail());

            String refreshToken = createRefreshToken(user).getRefreshToken();
            return new AuthResponse(accessToken, refreshToken, "Login successful");
        } catch (Exception e) {
            return new AuthResponse(null,null, e.getMessage());
        }
    }

    public AuthResponse refreshToken(TokenRefresh request) {
        try {
            String refreshTokenStr = request.getRefreshToken();
            return refreshTokenRepository.findByToken(refreshTokenStr)
                    .map(token -> {
                        if (token.getExpiryDate().isBefore(Instant.now())) {
                            refreshTokenRepository.delete(token);
                            return new AuthResponse(null, null, "token expired");
                        }
                        String newAccessToken = jwtUtils.generateAccessToken(token.getUser().getEmail());
                        return new AuthResponse(newAccessToken, refreshTokenStr, "Refresh successful");
                    })
                    .orElse(new AuthResponse(null, null, "Invalid refresh token"));
        } catch (Exception e) {
            return new AuthResponse(null, null,  "Internal Server Error");
        }
    }

    @Transactional
    public AuthResponse logout(User user) {
        try {
            refreshTokenRepository.deleteByUser(user);
            return new AuthResponse(null, null, "Logout successful");
        } catch (Exception e) {
            return new AuthResponse(null, null, "Internal Server Error");
        }
    }

    private AuthResponse createRefreshToken(User user) {
        try {
            refreshTokenRepository.deleteByUser(user); // Delete old if exists

            RefreshToken token = new RefreshToken();
            token.setUser(user);
            token.setToken(UUID.randomUUID().toString());
            token.setExpiryDate(Instant.now().plusMillis(refreshExpirationMs));
            refreshTokenRepository.save(token);
            return new AuthResponse(null, token.getToken(), "Created Refresh successfully");
        } catch (Exception e) {
            return new AuthResponse(null, null, "Internal Server Error");
        }
    }

    public User getUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }
}
