package com.example.BookingSystem.controller;

import com.example.BookingSystem.entity.ServiceEntity;
import com.example.BookingSystem.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/services")
@PreAuthorize("hasRole('ADMIN')")
public class AdminServiceController {

    @Autowired
    private ServiceService serviceService;

    @GetMapping
    public ResponseEntity<List<ServiceEntity>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAll());
    }


    @PostMapping
    public ResponseEntity<ServiceEntity> addService(@RequestBody ServiceEntity serviceEntity) {
        return ResponseEntity.ok(serviceService.create(serviceEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceEntity> updateService(@PathVariable Long id, @RequestBody ServiceEntity serviceEntity) {
        return ResponseEntity.ok(serviceService.update(id, serviceEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
