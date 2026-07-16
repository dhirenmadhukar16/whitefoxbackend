package com.example.whitefox.garments.repository;

import com.example.whitefox.garments.entity.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, UUID> {
    Optional<ServiceCategory> findByName(String name);
    List<ServiceCategory> findAllByOrderByCreatedAtDesc();
}
