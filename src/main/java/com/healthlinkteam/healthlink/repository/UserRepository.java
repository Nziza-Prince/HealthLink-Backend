package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.User;
import com.healthlinkteam.healthlink.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findByRole(UserRole role);
    List<User> findByIsActiveTrue();
    List<User> findByRoleAndIsActiveTrue(UserRole role);
    List<User> findByDepartment(String department);
    User findByEmail(String email);
}