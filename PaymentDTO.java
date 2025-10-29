package com.example.BookingSystem.dto;

import com.example.BookingSystem.entity.Payment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Long id;
    private Long bookingId;
    private Long userId;
    private Long amount;
    private String status;
    private String stripeSessionId;
    private LocalDateTime createdAt;

    public PaymentDTO(Payment p) {
        this.id = p.getId();
        this.bookingId = p.getBooking() != null ? p.getBooking().getId() : null;
        this.userId = p.getUser() != null ? p.getUser().getId() : null;
        this.amount = p.getAmount();
        this.status = p.getStatus();
        this.stripeSessionId = p.getStripeSessionId();
        this.createdAt = p.getCreatedAt();
    }
}
