package com.example.BookingSystem.controller;

import com.example.BookingSystem.entity.ServiceEntity;
import com.example.BookingSystem.entity.Slot;
import com.example.BookingSystem.repository.ServiceRepository;
import com.example.BookingSystem.repository.SlotRepository;
import com.example.BookingSystem.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class PublicServiceController {

    private final ServiceRepository serviceRepository;
    private final SlotRepository slotRepository;

    @GetMapping("/search")
    public List<ServiceEntity> searchServices(@RequestParam(required = false) String location,
                                              @RequestParam(required = false) String type) {
        return serviceRepository.searchByFilters(location, type);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceEntity> getServiceDetails(@PathVariable Long id) {
        return serviceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/slots")
    public List<Slot> getAvailableSlots(@PathVariable Long id,
                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return slotRepository.findAll().stream()
                .filter(slot -> !slot.isBooked() && slot.getServiceEntity().getId().equals(id))
                .filter(slot -> (start == null || !slot.getStartTime().isBefore(start)) &&
                        (end == null || !slot.getEndTime().isAfter(end)))
                .toList();
    }

    @Autowired
    private ServiceService serviceService;

    @GetMapping
    public ResponseEntity<List<ServiceEntity>> getServices() {
        return ResponseEntity.ok(serviceService.getAll());
    }
}
