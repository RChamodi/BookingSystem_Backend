package com.example.BookingSystem.service;

import com.example.BookingSystem.dto.SlotResponseDTO;
import com.example.BookingSystem.entity.ServiceEntity;
import com.example.BookingSystem.entity.Slot;
import com.example.BookingSystem.repository.ServiceRepository;
import com.example.BookingSystem.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SlotService {

    private final SlotRepository slotRepository;
    private final ServiceRepository serviceRepository;

    public Slot createSlot(Long serviceId, LocalDateTime start, LocalDateTime end) {
        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        Slot slot = Slot.builder()
                .serviceEntity(service)
                .startTime(start)
                .endTime(end)
                .booked(false)
                .build();

        return slotRepository.save(slot);
    }

    public List<SlotResponseDTO> getAvailableSlots(Long serviceId) {
        return slotRepository.findByServiceEntityIdAndBookedFalse(serviceId)
                .stream()
                .map(slot -> new SlotResponseDTO(
                        slot.getId(),
                        slot.getStartTime(),
                        slot.getEndTime()
                ))
                .toList();
    }



    public void markSlotAsBooked(Long slotId) {
        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        slot.setBooked(true);
        slotRepository.save(slot);
    }
    public List<Slot> getAllSlotsForService(Long serviceId) {
        return slotRepository.findByServiceEntityId(serviceId);
    }

    public void deleteSlot(Long slotId) {
        slotRepository.deleteById(slotId);
    }

    public List<Slot> getAllSlots() {
        return slotRepository.findAll();
    }
}
