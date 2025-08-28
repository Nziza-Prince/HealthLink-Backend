package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    Department findDepartmentByName(String name);
}
