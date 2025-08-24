package com.healthlinkteam.healthlink.controller;

import com.healthlinkteam.healthlink.dto.CreateUserDTO;
import com.healthlinkteam.healthlink.dto.UserDTO;
import com.healthlinkteam.healthlink.entity.Doctor;
import com.healthlinkteam.healthlink.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllStaff() {
        return ResponseEntity.ok(staffService.all());
    }

    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        return ResponseEntity.ok(staffService.create(doctor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable UUID id, @RequestBody Doctor doctor) {
        return ResponseEntity.ok(staffService.update(id, doctor));
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateDoctor(@PathVariable UUID id) {
        staffService.deactivate(id);
        return ResponseEntity.ok("Doctor deactivated successfully");
    }
}
