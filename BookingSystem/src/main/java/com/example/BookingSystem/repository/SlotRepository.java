package com.example.BookingSystem.repository;

import com.example.BookingSystem.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByServiceEntityIdAndBookedFalse(Long serviceId);
    List<Slot> findByServiceEntityId(Long serviceId);
    @Query("SELECT s FROM Slot s WHERE s.booked = false AND " +
            "(:start IS NULL OR s.startTime >= :start) AND " +
            "(:end IS NULL OR s.endTime <= :end)")
    List<Slot> findAvailableSlots(@Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end);


}
