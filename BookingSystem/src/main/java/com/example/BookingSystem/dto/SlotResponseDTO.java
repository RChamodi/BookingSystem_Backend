package com.example.BookingSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SlotResponseDTO {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
