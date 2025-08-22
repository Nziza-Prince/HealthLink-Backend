package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.entity.Doctor;
import com.healthlinkteam.healthlink.repository.DoctorRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StaffService {
    private final DoctorRepository repo;


    public List<Doctor> all() { return repo.findAll(); }
    public Doctor get(Long id) { return repo.findById(id).orElseThrow(); }
    public Doctor create(Doctor d) { return repo.save(d); }
    public Doctor update(Long id, Doctor d) {
        Doctor cur = get(id);
        cur.setName(d.getName());
        cur.setRole(d.getRole());
        cur.setJoinedDate(d.getJoinedDate());
        cur.setEmail(d.getEmail());
        cur.setDepartment(d.getDepartment());
        cur.setEndDate(d.getEndDate());
        return repo.save(cur);
    }
    public void deactivate(Long id) {
        Doctor cur = get(id);
        cur.setActive(false);
        repo.save(cur);
    }
}