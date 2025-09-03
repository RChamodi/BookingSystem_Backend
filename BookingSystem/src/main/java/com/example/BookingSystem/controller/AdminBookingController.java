package com.example.BookingSystem.controller;

import com.example.BookingSystem.dto.BookingAdminDTO;
import com.example.BookingSystem.entity.Booking;
import com.example.BookingSystem.repository.BookingRepository;
import com.example.BookingSystem.service.BookingService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/bookings")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminBookingController {

    private final BookingRepository bookingRepository;

    @GetMapping
    public ResponseEntity<List<BookingAdminDTO>> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingAdminDTO> dtos = bookings.stream().map(b -> {
            BookingAdminDTO dto = new BookingAdminDTO();
            dto.setId(b.getId());
            dto.setUser(b.getUser().getName());
            dto.setService(b.getServiceEntity().getName());
            dto.setLocation(b.getLocation());
            dto.setDateTime(b.getDateTime());
            dto.setApproved(b.isApproved());
            dto.setCancelled(b.isCancelled());
            return dto;
        }).toList();

        return ResponseEntity.ok(dtos);
    }


    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approveBooking(@PathVariable Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setApproved(true);
        bookingRepository.save(booking);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setCancelled(true);
        bookingRepository.save(booking);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking updated) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setDateTime(updated.getDateTime());
        booking.setLocation(updated.getLocation());
        return ResponseEntity.ok(bookingRepository.save(booking));
    }
}
