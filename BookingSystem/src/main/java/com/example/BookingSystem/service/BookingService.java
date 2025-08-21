package com.example.BookingSystem.service;

import com.example.BookingSystem.dto.BookingResponseDTO;
import com.example.BookingSystem.entity.Booking;
import com.example.BookingSystem.entity.ServiceEntity;
import com.example.BookingSystem.entity.Slot;
import com.example.BookingSystem.entity.User;
import com.example.BookingSystem.repository.BookingRepository;
import com.example.BookingSystem.repository.ServiceRepository;
import com.example.BookingSystem.repository.SlotRepository;
import com.example.BookingSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final SlotRepository slotRepository;


    public Booking createBooking(Long slotId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (slot.isBooked()) {
            throw new RuntimeException("Slot already booked");
        }

        slot.setBooked(true);
        slotRepository.save(slot);

        ServiceEntity serviceEntity = slot.getServiceEntity();

        Booking booking = Booking.builder()
                .user(user)
                .slot(slot)
                .serviceEntity(serviceEntity)
                .dateTime(slot.getStartTime())
                .location("Online or at " + serviceEntity.getLocation())
                .approved(false)
                .cancelled(false)
                .build();

        return bookingRepository.save(booking);
    }

    public List<BookingResponseDTO> getBookingsForUser(String email) {
        List<Booking> bookings = bookingRepository.findAllByUserEmail(email);
        return bookings.stream().map(booking -> {
            BookingResponseDTO dto = new BookingResponseDTO();
            dto.setId(booking.getId());
            dto.setServiceName(booking.getServiceEntity().getName());
            dto.setLocation(booking.getLocation());
            dto.setDateTime(booking.getDateTime());
            dto.setCancelled(booking.isCancelled());
            return dto;
        }).toList();
    }


    public void cancelBookingByUser(Long bookingId, String email) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }

        booking.setCancelled(true);


        if (booking.getSlot() != null) {
            Slot slot = booking.getSlot();
            slot.setBooked(false);
            slotRepository.save(slot);
        }

        bookingRepository.save(booking);
    }
}
