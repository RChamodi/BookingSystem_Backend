package com.example.BookingSystem.service;

import com.example.BookingSystem.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {
    private final BookingRepository bookingRepository;

    public long getTotalBookings() {
        return bookingRepository.count();
    }

    public long getApprovedBookings() {
        return bookingRepository.countByApproved(true);
    }

    public long getCancelledBookings() {
        return bookingRepository.countByCancelled(true);
    }

    public List<Object[]> getBookingsPerDay() {
        return bookingRepository.countBookingsGroupedByDay();
    }

    public List<Object[]> getBookingsPerService() {
        return bookingRepository.countBookingsGroupedByService();
    }

}
