package com.example.whitefox.garments.repository;





import com.example.whitefox.garments.entity.ServiceCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

public interface ServiceCatalogRepository
        extends JpaRepository<ServiceCatalog, UUID> {

    List<ServiceCatalog> findByActiveTrue();

    List<ServiceCatalog> findByServiceTypeAndActiveTrue(
            String serviceType
    );
    Optional<ServiceCatalog> findById(UUID id);
//    boolean existsByCode(String code);
}