package com.example.BookingSystem.controller;

import com.example.BookingSystem.dto.PaymentDTO;
import com.example.BookingSystem.service.PaymentService;
import com.example.BookingSystem.entity.Payment;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Value("${frontend.success.url}")
    private String successUrl;

    @Value("${frontend.cancel.url}")
    private String cancelUrl;

    // Create Stripe Checkout session (using booking's price)
    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(@RequestBody CreateSessionRequest req) {
        try {
            Session session = paymentService.createCheckoutSession(req.getBookingId(), successUrl, cancelUrl);
            return ResponseEntity.ok(new CreateSessionResponse(session.getId(), session.getUrl()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error creating Stripe session: " + ex.getMessage());
        }
    }

    // Retrieve session by ID (from success page)
    @GetMapping("/session/{id}")
    public ResponseEntity<Session> getSession(@PathVariable String id) {
        try {
            Session session = paymentService.getSession(id);
            return ResponseEntity.ok(session);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    //  Mark booking as paid (called by frontend after payment success)
    @PutMapping("/bookings/mark-paid/{bookingId}")
    public ResponseEntity<Void> markBookingPaid(@PathVariable Long bookingId) {
        try {
            paymentService.markBookingAsPaid(bookingId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    //  Admin: View payments
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentDTO>> getPayments(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String userEmail) {

        List<Payment> payments = paymentService.getPayments(from, to, userEmail);
        List<PaymentDTO> dtos = payments.stream().map(PaymentDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }


    //  Admin: Export CSV
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/export")
    public ResponseEntity<byte[]> exportPaymentsCsv(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String userEmail) {

        try {
            List<Payment> payments = paymentService.getPayments(from, to, userEmail);
            byte[] csvBytes = paymentService.exportPaymentsCsv(payments).readAllBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"payments-report.csv\"");

            return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Request/Response DTOs
    static class CreateSessionRequest {
        private Long bookingId;

        public Long getBookingId() {
            return bookingId;
        }

        public void setBookingId(Long bookingId) {
            this.bookingId = bookingId;
        }
    }

    static class CreateSessionResponse {
        private String sessionId;
        private String url;

        public CreateSessionResponse(String sessionId, String url) {
            this.sessionId = sessionId;
            this.url = url;
        }

        public String getSessionId() {
            return sessionId;
        }

        public String getUrl() {
            return url;
        }
    }
}
