package com.example.BookingSystem.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingAdminDTO {
    private Long id;
    private String user;
    private String service;
    private String location;
    private LocalDateTime dateTime;
    private boolean approved;
    private boolean cancelled;
}
