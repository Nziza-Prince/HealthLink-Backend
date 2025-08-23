package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.management.relation.Role;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole(Role role);
    List<User> findByIsActiveTrue();
    List<User> findByRoleAndIsActiveTrue(Role role);
    List<User> findByDepartment(String department);
    User findByEmail(String email);
}