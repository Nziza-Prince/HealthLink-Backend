package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    /**
     * Find patient by email
     */
    @Query("SELECT d FROM Doctor d WHERE d.email = :email")
    Optional<Doctor> findByEmail(@Param("email") String email);

    /**
     * Check if patient exists by email
     */
    @Query("SELECT COUNT(d) > 0 FROM Doctor d WHERE d.email = :email")
    boolean existsByEmail(@Param("email") String email);

    /**
     * Find patient by phone number
     */
    @Query("SELECT d FROM Doctor d WHERE d.phoneNumber = :phoneNumber")
    Optional<Doctor> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
