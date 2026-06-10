package com.example.whitefox.tracking.repository;



import com.example.whitefox.tracking.entity.Garment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GarmentRepository
        extends JpaRepository<Garment, UUID> {

    List<Garment> findByOrderId(UUID orderId);

    Optional<Garment> findByStoreQrCode(String storeQrCode);

    Optional<Garment> findByOutingQrCode(String outingQrCode);
}
