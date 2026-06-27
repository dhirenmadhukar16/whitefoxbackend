package com.example.whitefox.trucklogistics.repository;

import com.example.whitefox.trucklogistics.entity.TruckTripStop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TruckTripStopRepository extends JpaRepository<TruckTripStop, UUID> {

    List<TruckTripStop> findByTripIdOrderByStopSequenceAsc(UUID tripId);

    List<TruckTripStop> findByStoreId(UUID storeId);
}