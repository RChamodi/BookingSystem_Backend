package com.example.BookingSystem.service;

import com.example.BookingSystem.entity.ServiceEntity;
import com.example.BookingSystem.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public ServiceEntity create(ServiceEntity serviceEntity) {
        return serviceRepository.save(serviceEntity);
    }

    public ServiceEntity update(Long id, ServiceEntity updatedServiceEntity) {
        ServiceEntity serviceEntity = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        serviceEntity.setName(updatedServiceEntity.getName());
        serviceEntity.setDescription(updatedServiceEntity.getDescription());
        serviceEntity.setPrice(updatedServiceEntity.getPrice());
        serviceEntity.setType(updatedServiceEntity.getType());
        serviceEntity.setLocation(updatedServiceEntity.getLocation());

        return serviceRepository.save(serviceEntity);
    }

    public void delete(Long id) {
        serviceRepository.deleteById(id);
    }

    public List<ServiceEntity> getAll() {
        return serviceRepository.findAll();
    }
}
