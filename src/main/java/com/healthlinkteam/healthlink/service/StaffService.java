package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.entity.Doctor;
import com.healthlinkteam.healthlink.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final DoctorRepository repo;

    public List<Doctor> all() {
        return repo.findAll();
    }

    public Doctor get(UUID id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    public Doctor create(Doctor d) {
        return repo.save(d);
    }

    public Doctor update(UUID id, Doctor d) {
        Doctor cur = get(id);
        cur.setFirstName(d.getFirstName());
        cur.setLastName(d.getLastName());
        cur.setRole(d.getRole());
        cur.setJoinedDate(d.getJoinedDate());
        cur.setEmail(d.getEmail());
        cur.setDepartment(d.getDepartment());
        cur.setEndDate(d.getEndDate());
        cur.setIsAvailable(d.getIsAvailable());
        return repo.save(cur);
    }

    public void deactivate(UUID id) {
        Doctor cur = get(id);
        cur.setIsActive(false);
        repo.save(cur);
    }
}
