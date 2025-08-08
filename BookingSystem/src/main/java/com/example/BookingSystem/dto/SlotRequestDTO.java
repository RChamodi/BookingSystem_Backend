package com.example.BookingSystem.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SlotRequestDTO {
    private Long serviceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
