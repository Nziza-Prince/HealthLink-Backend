package com.healthlinkteam.healthlink.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventory;


    @GetMapping
    public List<InventoryCardDTO> all() { return inventory.all().stream().map(DtoMappers::toDto).toList(); }


    @GetMapping("/{id}")
    public InventoryCardDTO get(@PathVariable Long id) { return DtoMappers.toDto(inventory.get(id)); }


    @PostMapping
    public InventoryCardDTO create(@Valid @RequestBody Medication m) { return DtoMappers.toDto(inventory.create(m)); }


    @PutMapping("/{id}")
    public InventoryCardDTO update(@PathVariable Long id, @Valid @RequestBody Medication m) { return DtoMappers.toDto(inventory.update(id, m)); }


    @PostMapping("/{id}/reorder")
    public InventoryCardDTO reorder(@PathVariable Long id, @RequestParam(defaultValue = "10") int qty) { return DtoMappers.toDto(inventory.reorder(id, qty)); }
}