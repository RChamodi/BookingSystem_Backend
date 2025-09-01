package com.example.BookingSystem.service;

import com.example.BookingSystem.entity.Booking;
import com.example.BookingSystem.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendBookingConfirmation(User user, Booking booking) {
        System.out.println("Sending booking confirmation to: " + user.getEmail());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Booking Confirmation");
        message.setText("Hi " + user.getName() + ",\n\nYour booking for " +
                booking.getServiceEntity().getName() + " is confirmed at " +
                booking.getDateTime() + ".");

        mailSender.send(message);
    }

    public void sendCancellation(User user, Booking booking) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Booking Cancelled");
        message.setText("Hi " + user.getName() + ",\n\nYour booking for " +
                booking.getServiceEntity().getName() + " has been cancelled.");

        mailSender.send(message);
    }
}

