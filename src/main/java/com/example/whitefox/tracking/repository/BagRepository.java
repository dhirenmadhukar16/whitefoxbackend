package com.example.whitefox.tracking.repository;

import com.example.whitefox.tracking.entity.Bag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BagRepository extends JpaRepository<Bag, UUID> {
    Optional<Bag> findByQrCode(String qrCode);
}
