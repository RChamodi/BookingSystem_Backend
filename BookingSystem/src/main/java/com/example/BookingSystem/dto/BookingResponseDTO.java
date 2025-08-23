package com.example.BookingSystem.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingResponseDTO {
    private Long id;
    private String serviceName;
    private String location;
    private LocalDateTime dateTime;
    private boolean cancelled;
}
