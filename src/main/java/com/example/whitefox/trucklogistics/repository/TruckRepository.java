package com.example.whitefox.trucklogistics.repository;

import com.example.whitefox.trucklogistics.entity.Truck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TruckRepository extends JpaRepository<Truck, UUID> {

    Optional<Truck> findByTruckNumber(String truckNumber);
}