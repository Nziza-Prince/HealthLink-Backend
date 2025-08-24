package com.healthlinkteam.healthlink.controller;

import com.healthlinkteam.healthlink.dto.CreateMedicalInventoryDTO;
import com.healthlinkteam.healthlink.dto.MedicalInventoryDTO;
import com.healthlinkteam.healthlink.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<MedicalInventoryDTO>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @PostMapping
    public ResponseEntity<MedicalInventoryDTO> addMedication(@RequestBody CreateMedicalInventoryDTO createDTO) {
        MedicalInventoryDTO saved = inventoryService.addMedication(createDTO);
        return ResponseEntity.created(URI.create("/api/inventory/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalInventoryDTO> updateInventory(@PathVariable UUID id,
                                                               @RequestBody CreateMedicalInventoryDTO updateDTO) {
        return ResponseEntity.ok(inventoryService.updateInventory(id, updateDTO));
    }

    @PostMapping("/{id}/reorder")
    public ResponseEntity<String> reorderMedication(@PathVariable UUID id) {
        inventoryService.reorderMedication(id);
        return ResponseEntity.ok("Reorder initiated successfully");
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<MedicalInventoryDTO>> getLowStockItems() {
        return ResponseEntity.ok(inventoryService.getLowStockItems());
    }

    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<MedicalInventoryDTO>> getInventoryByStatus(@PathVariable String status) {
        return ResponseEntity.ok(inventoryService.getInventoryByStatus(status));
    }
}
