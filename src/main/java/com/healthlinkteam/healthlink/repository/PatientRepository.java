package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    /**
     * Find patient by email
     */
    @Query("SELECT p FROM Patient p WHERE p.email = :email")
    Optional<Patient> findByEmail(@ Param("email") String email);

    /**
     * Check if patient exists by email
     */
    @Query("SELECT COUNT(p) > 0 FROM Patient p WHERE p.email = :email")
    boolean existsByEmail(@Param("email") String email);

    /**
     * Find patient by phone number
     */
    @Query("SELECT p FROM Patient p WHERE p.phoneNumber = :phoneNumber")
    Optional<Patient> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    List<Patient> findByDoctorIdOrderByServiceDateAsc(UUID doctorId);
}
