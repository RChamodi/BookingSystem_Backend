package com.example.BookingSystem.repository;

import com.example.BookingSystem.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    @Query("SELECT s FROM ServiceEntity s WHERE " +
            "(:location IS NULL OR s.location = :location) AND " +
            "(:type IS NULL OR s.type = :type)")
    List<ServiceEntity> searchByFilters(@Param("location") String location,
                                        @Param("type") String type);
}
