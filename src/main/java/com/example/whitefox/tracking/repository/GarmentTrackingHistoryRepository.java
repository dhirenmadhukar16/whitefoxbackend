package com.example.whitefox.tracking.repository;



import com.example.whitefox.tracking.entity.GarmentTrackingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GarmentTrackingHistoryRepository
        extends JpaRepository<GarmentTrackingHistory, UUID> {

    List<GarmentTrackingHistory> findByGarmentId(UUID garmentId);
}
