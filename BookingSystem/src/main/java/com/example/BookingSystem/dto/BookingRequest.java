package com.example.BookingSystem.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequest {
    private String serviceName;
    private String location;
    private LocalDateTime dateTime;
}
