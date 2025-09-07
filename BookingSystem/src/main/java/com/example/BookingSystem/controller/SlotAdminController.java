package com.example.BookingSystem.controller;

import com.example.BookingSystem.dto.SlotRequestDTO;
import com.example.BookingSystem.entity.Slot;
import com.example.BookingSystem.service.SlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/slots")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class SlotAdminController {

    private final SlotService slotService;


    @PostMapping
    public ResponseEntity<Slot> createSlot(@RequestBody SlotRequestDTO request) {
        Slot slot = slotService.createSlot(request.getServiceId(), request.getStartTime(), request.getEndTime());
        return ResponseEntity.ok(slot);
    }


    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<Slot>> getSlotsByService(@PathVariable Long serviceId) {
        return ResponseEntity.ok(slotService.getAllSlotsForService(serviceId));
    }


    @GetMapping
    public ResponseEntity<List<Slot>> getAllSlots() {
        return ResponseEntity.ok(slotService.getAllSlots());
    }


    @DeleteMapping("/{slotId}")
    public ResponseEntity<Void> deleteSlot(@PathVariable Long slotId) {
        slotService.deleteSlot(slotId);
        return ResponseEntity.noContent().build();
    }
}
