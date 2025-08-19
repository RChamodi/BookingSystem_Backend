package com.example.BookingSystem.repository;

import com.example.BookingSystem.entity.Booking;
import com.example.BookingSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByUserEmail(String email);
    long countByApproved(boolean approved);
    long countByCancelled(boolean cancelled);

    @Query("SELECT DATE(b.dateTime), COUNT(b) FROM Booking b GROUP BY DATE(b.dateTime) ORDER BY DATE(b.dateTime)")
    List<Object[]> countBookingsGroupedByDay();

    @Query("SELECT b.serviceEntity.name, COUNT(b) FROM Booking b GROUP BY b.serviceEntity.name")
    List<Object[]> countBookingsGroupedByService();

}
