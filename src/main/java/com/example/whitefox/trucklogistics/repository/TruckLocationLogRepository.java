package com.example.whitefox.trucklogistics.repository;

import com.example.whitefox.trucklogistics.entity.TruckLocationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TruckLocationLogRepository extends JpaRepository<TruckLocationLog, UUID> {

    List<TruckLocationLog> findByTruckIdOrderByRecordedAtDesc(UUID truckId);

    List<TruckLocationLog> findByTripIdOrderByRecordedAtDesc(UUID tripId);
}