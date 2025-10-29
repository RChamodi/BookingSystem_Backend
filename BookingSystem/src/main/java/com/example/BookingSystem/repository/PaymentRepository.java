package com.example.BookingSystem.repository;

import com.example.BookingSystem.entity.Booking;
import com.example.BookingSystem.entity.Payment;
import com.example.BookingSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByStripeSessionId(String sessionId);
    List<Payment> findByUser(User user);
    List<Payment> findByBooking(Booking booking);
    Optional<Payment> findTopByBookingIdOrderByCreatedAtDesc(Long bookingId);
}

