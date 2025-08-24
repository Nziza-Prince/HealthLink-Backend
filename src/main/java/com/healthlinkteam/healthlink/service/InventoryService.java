package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.dto.CreateMedicalInventoryDTO;
import com.healthlinkteam.healthlink.dto.MedicalInventoryDTO;
import com.healthlinkteam.healthlink.entity.MedicalInventory;
import com.healthlinkteam.healthlink.enums.NotificationType;
import com.healthlinkteam.healthlink.enums.StockStatus;
import com.healthlinkteam.healthlink.repository.MedicalInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    @Autowired
    private MedicalInventoryRepository inventoryRepository;

    @Autowired
    private NotificationService notificationService;

    public List<MedicalInventoryDTO> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicalInventoryDTO> getInventoryByStatus(String status) {
        StockStatus stockStatus = StockStatus.valueOf(status.toUpperCase());
        return inventoryRepository.findByStatus(stockStatus).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MedicalInventoryDTO addMedication(CreateMedicalInventoryDTO createDTO) {
        MedicalInventory inventory = new MedicalInventory();
        inventory.setMedicineName(createDTO.getMedicineName());
        inventory.setCategory(createDTO.getCategory());
        inventory.setStockQuantity(createDTO.getStockQuantity());
        inventory.setStockExpiryDate(createDTO.getStockExpiryDate());
        inventory.setSupplierName(createDTO.getSupplierName());
        inventory.setSupplierContact(createDTO.getSupplierContact());
        inventory.setStorageInstructions(createDTO.getStorageInstructions());
        inventory.setLastRestocked(LocalDateTime.now());

        inventory.updateStockStatus();

        MedicalInventory saved = inventoryRepository.save(inventory);

        // Notify if low stock
        if (saved.getStatus() == StockStatus.LOW_STOCK) {
            notificationService.sendSystemNotification(
                    "Low stock alert: " + saved.getMedicineName() +
                            " (" + saved.getStockQuantity() + " remaining)",
                    NotificationType.LOW_STOCK
            );
        }

        return convertToDTO(saved);
    }

    public MedicalInventoryDTO updateInventory(UUID id, CreateMedicalInventoryDTO updateDTO) {
        MedicalInventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory item not found"));

        inventory.setMedicineName(updateDTO.getMedicineName());
        inventory.setCategory(updateDTO.getCategory());
        inventory.setStockQuantity(updateDTO.getStockQuantity());
        inventory.setStockExpiryDate(updateDTO.getStockExpiryDate());
        inventory.setSupplierName(updateDTO.getSupplierName());
        inventory.setSupplierContact(updateDTO.getSupplierContact());
        inventory.setStorageInstructions(updateDTO.getStorageInstructions());

        inventory.updateStockStatus();

        MedicalInventory saved = inventoryRepository.save(inventory);

        // Notify if stock drops to low
        if (saved.getStatus() == StockStatus.LOW_STOCK) {
            notificationService.sendSystemNotification(
                    "Low stock alert: " + saved.getMedicineName() +
                            " (" + saved.getStockQuantity() + " remaining)",
                    NotificationType.LOW_STOCK
            );
        }

        return convertToDTO(saved);
    }

    public void reorderMedication(UUID id) {
        MedicalInventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory item not found"));

        inventory.setStatus(StockStatus.PENDING);
        inventoryRepository.save(inventory);

        notificationService.sendSystemNotification(
                "Reorder initiated for: " + inventory.getMedicineName() +
                        " from supplier: " + inventory.getSupplierName(),
                NotificationType.GENERAL
        );
    }

    public List<MedicalInventoryDTO> getLowStockItems() {
        return inventoryRepository.findByStatus(StockStatus.LOW_STOCK).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void checkLowStockAndNotify() {
        List<MedicalInventory> lowStockItems = inventoryRepository.findByStatus(StockStatus.LOW_STOCK);
        for (MedicalInventory item : lowStockItems) {
            notificationService.sendSystemNotification(
                    "Low stock alert: " + item.getMedicineName() +
                            " (" + item.getStockQuantity() + " remaining)",
                    NotificationType.LOW_STOCK
            );
        }
    }

    private MedicalInventoryDTO convertToDTO(MedicalInventory inventory) {
        MedicalInventoryDTO dto = new MedicalInventoryDTO();
        dto.setId(inventory.getId());
        dto.setMedicineName(inventory.getMedicineName());
        dto.setCategory(inventory.getCategory());
        dto.setStockQuantity(inventory.getStockQuantity());
        dto.setStockExpiryDate(inventory.getStockExpiryDate());
        dto.setSupplierName(inventory.getSupplierName());
        dto.setSupplierContact(inventory.getSupplierContact());
        dto.setStorageInstructions(inventory.getStorageInstructions());
        dto.setStatus(inventory.getStatus().toString());
        dto.setLastRestocked(inventory.getLastRestocked());
        return dto;
    }
}
