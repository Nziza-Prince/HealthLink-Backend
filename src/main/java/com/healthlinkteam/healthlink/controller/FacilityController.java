package com.healthlinkteam.healthlink.controller;



import com.healthlinkteam.healthlink.entity.Facility;
import com.healthlinkteam.healthlink.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/facilities")
@RequiredArgsConstructor
public class FacilityController {
    private final FacilityRepository repo;


    @GetMapping public List<Facility> all() { return repo.findAll(); }
    @PostMapping public Facility create(@RequestBody Facility f) { return repo.save(f); }
    @PutMapping("/{id}") public Facility update(@PathVariable Long id, @RequestBody Facility f) {
        Facility cur = repo.findById(id).orElseThrow();
        cur.setName(f.getName());
        cur.setDescription(f.getDescription());
        return repo.save(cur);
    }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { repo.deleteById(id); }
}