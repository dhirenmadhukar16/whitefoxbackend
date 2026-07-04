package com.example.whitefox.DeliveryOtp.repository;

import com.example.whitefox.DeliveryOtp.entity.DeliveryOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryOtpRepository
        extends JpaRepository<DeliveryOtp, UUID> {

    Optional<DeliveryOtp> findTopByOrderIdOrderByCreatedAtDesc(
            UUID orderId
    );
}