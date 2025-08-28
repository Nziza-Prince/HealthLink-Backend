package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.User;
import com.healthlinkteam.healthlink.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    List<User> findByRole(UserRole role);
    List<User> findByIsActiveTrue();

    @Query("SELECT u FROM User u WHERE u.role = :role AND u.isActive = true")
    List<User> findByRoleAndIsActiveTrue(UserRole role, Boolean isActive);
    Optional<User> findByEmail(String email);
}