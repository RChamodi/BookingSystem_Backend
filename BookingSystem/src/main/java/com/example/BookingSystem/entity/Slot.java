package com.example.BookingSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private boolean booked = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    @JsonIgnore
    private ServiceEntity serviceEntity;
}
