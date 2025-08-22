package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.entity.Medication;
import com.healthlinkteam.healthlink.repository.MedicationRepository;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class InventoryService {
    private final MedicationRepository repo;


    public List<Medication> all() { return repo.findAll(); }
    public Medication get(Long id) { return repo.findById(id).orElseThrow(); }
    public Medication create(Medication m) { return repo.save(m); }
    public Medication update(Long id, Medication m) {
        Medication cur = get(id);
        cur.setName(m.getName());
        cur.setStockLevel(m.getStockLevel());
        cur.setSupplier(m.getSupplier());
        cur.setStatus(m.getStatus());
        cur.setLastRestocked(m.getLastRestocked());
        return repo.save(cur);
    }


    public Medication reorder(Long id, int qty) {
        Medication cur = get(id);
        cur.setStockLevel(cur.getStockLevel() + qty);
        cur.setStatus(cur.getStockLevel() == 0 ? InventoryStatus.OUT_OF_STOCK : InventoryStatus.IN_STOCK);
        cur.setLastRestocked(LocalDate.now());
        return repo.save(cur);
    }
}
