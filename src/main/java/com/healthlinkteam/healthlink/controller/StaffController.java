package com.healthlinkteam.healthlink.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {
    private final StaffService staff;


    @GetMapping
    public List<StaffCardDTO> all() { return staff.all().stream().map(DtoMappers::toDto).toList(); }


    @GetMapping("/{id}")
    public StaffCardDTO get(@PathVariable Long id) { return DtoMappers.toDto(staff.get(id)); }


    @PostMapping
    public StaffCardDTO create(@Valid @RequestBody Doctor d) { return DtoMappers.toDto(staff.create(d)); }


    @PutMapping("/{id}")
    public StaffCardDTO update(@PathVariable Long id, @Valid @RequestBody Doctor d) { return DtoMappers.toDto(staff.update(id, d)); }


    @PostMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) { staff.deactivate(id); }
}