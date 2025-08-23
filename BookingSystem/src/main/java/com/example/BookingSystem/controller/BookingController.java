package com.example.BookingSystem.controller;

import com.example.BookingSystem.dto.BookingResponseDTO;
import com.example.BookingSystem.entity.Booking;
import com.example.BookingSystem.entity.User;
import com.example.BookingSystem.service.BookingService;
import com.example.BookingSystem.service.UserService;
import com.example.BookingSystem.dto.BookingRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest request, Authentication auth) {
        Booking booking = bookingService.createBooking(request.getSlotId(), auth.getName());
        return ResponseEntity.ok(booking);
    }

    @Data
    public static class BookingRequest {
        private Long slotId;
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<BookingResponseDTO>> getMyBookings(Authentication auth) {
        return ResponseEntity.ok(bookingService.getBookingsForUser(auth.getName()));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> cancelMyBooking(@PathVariable Long id, Authentication auth) {
        bookingService.cancelBookingByUser(id, auth.getName());
        return ResponseEntity.noContent().build();
    }


}

