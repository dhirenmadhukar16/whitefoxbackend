package com.example.whitefox.trucklogistics.repository;

import com.example.whitefox.trucklogistics.entity.TruckTrip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TruckTripRepository extends JpaRepository<TruckTrip, UUID> {

    List<TruckTrip> findByTruckId(UUID truckId);
}