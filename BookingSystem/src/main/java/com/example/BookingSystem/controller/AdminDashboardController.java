package com.example.BookingSystem.controller;

import com.example.BookingSystem.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")

public class AdminDashboardController {
    private final AdminDashboardService dashboardService;

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBookings", dashboardService.getTotalBookings());
        stats.put("approvedBookings", dashboardService.getApprovedBookings());
        stats.put("cancelledBookings", dashboardService.getCancelledBookings());
        return stats;
    }

    @GetMapping("/bookings-per-day")
    public List<Map<String, Object>> getBookingsPerDay() {
        return dashboardService.getBookingsPerDay().stream().map(row -> {
            Map<String, Object> m = new HashMap<>();
            m.put("date", row[0]);
            m.put("count", row[1]);
            return m;
        }).toList();
    }

    @GetMapping("/bookings-per-service")
    public List<Map<String, Object>> getBookingsPerService() {
        return dashboardService.getBookingsPerService().stream().map(row -> {
            Map<String, Object> m = new HashMap<>();
            m.put("service", row[0]);
            m.put("count", row[1]);
            return m;
        }).toList();
    }

}
