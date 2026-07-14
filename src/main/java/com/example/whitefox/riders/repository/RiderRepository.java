package com.example.whitefox.riders.repository;



import com.example.whitefox.riders.entity.Rider;
import com.example.whitefox.riders.enums.RiderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RiderRepository extends JpaRepository<Rider, UUID> {

    Optional<Rider> findByPhone(String phone);
    Optional<Rider> findByEmail(String email);

    List<Rider> findByStatusAndActiveTrue(RiderStatus status);
}
