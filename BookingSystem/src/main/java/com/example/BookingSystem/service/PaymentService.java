package com.example.BookingSystem.service;

import com.example.BookingSystem.entity.Booking;
import com.example.BookingSystem.entity.Payment;
import com.example.BookingSystem.repository.BookingRepository;
import com.example.BookingSystem.repository.PaymentRepository;
import com.example.BookingSystem.repository.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    //  Create Stripe checkout session
    public Session createCheckoutSession(Long bookingId, String successUrl, String cancelUrl) throws Exception {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new Exception("Booking not found"));

        long amount = Math.round(booking.getServiceEntity().getPrice() * 100);


        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(cancelUrl)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("inr")
                                                .setUnitAmount(amount)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Service: " + booking.getServiceEntity().getName())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .putMetadata("bookingId", booking.getId().toString())
                .putMetadata("userId", booking.getUser().getId().toString())
                .build();

        Session session = Session.create(params);

        // Save a "PENDING" payment
        Payment payment = Payment.builder()
                .booking(booking)
                .user(booking.getUser())
                .amount(amount)
                .status("PENDING")
                .stripeSessionId(session.getId())
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);
        return session;
    }

    // Mark booking as paid (from frontend)
    public void markBookingAsPaid(Long bookingId) throws Exception {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new Exception("Booking not found"));

        // Update booking (if you want to flag it as paid)
        booking.setApproved(true); // optional
        bookingRepository.save(booking);

        // Update payment record
        Payment payment = paymentRepository.findTopByBookingIdOrderByCreatedAtDesc(bookingId)
                .orElseThrow(() -> new Exception("Payment record not found"));

        payment.setStatus("SUCCEEDED");
        paymentRepository.save(payment);
    }


    public Session getSession(String sessionId) throws StripeException {
        return Session.retrieve(sessionId);
    }

    //  Get payments (admin/report)
    public List<Payment> getPayments(String fromDate, String toDate, String userEmail) {
        // Add filtering logic as needed
        return paymentRepository.findAll(); // simple version
    }

    //  Export payments to CSV
    public ByteArrayInputStream exportPaymentsCsv(List<Payment> payments) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        org.apache.commons.csv.CSVPrinter printer = new org.apache.commons.csv.CSVPrinter(
                new java.io.OutputStreamWriter(out),
                org.apache.commons.csv.CSVFormat.DEFAULT.withHeader(
                        "Payment ID", "Booking ID", "User ID", "Amount", "Status", "Session ID", "Created At"
                )
        );

        for (Payment p : payments) {
            printer.printRecord(
                    p.getId(),
                    p.getBooking().getId(),
                    p.getUser().getId(),
                    p.getAmount(),
                    p.getStatus(),
                    p.getStripeSessionId(),
                    p.getCreatedAt()
            );
        }

        printer.flush();
        return new ByteArrayInputStream(out.toByteArray());
    }
}
