package com.example.BookingSystem.controller;

import com.example.BookingSystem.dto.SlotResponseDTO;
import com.example.BookingSystem.entity.Booking;
import com.example.BookingSystem.entity.User;
import com.example.BookingSystem.service.BookingService;
import com.example.BookingSystem.service.EmailService;
import com.example.BookingSystem.service.SlotService;
import com.example.BookingSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/slots")
@RequiredArgsConstructor
public class UserSlotController {

    private final SlotService slotService;
    private final BookingService bookingService;
    private final EmailService emailService;
    private final UserService userService;

    @GetMapping("/available/{serviceId}")
    public ResponseEntity<List<SlotResponseDTO>> getAvailableSlots(@PathVariable Long serviceId) {
        return ResponseEntity.ok(slotService.getAvailableSlots(serviceId));
    }

    @PostMapping("/book/{slotId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Booking> bookSlot(@PathVariable Long slotId, Authentication auth) {
        Booking booking = bookingService.createBooking(slotId, auth.getName());
        User user = userService.findByEmail(auth.getName());
        emailService.sendBookingConfirmation(user, booking);
        return ResponseEntity.ok(booking);
    }
}
