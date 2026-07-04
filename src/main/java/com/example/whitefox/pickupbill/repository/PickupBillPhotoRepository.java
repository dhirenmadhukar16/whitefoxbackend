package com.example.whitefox.pickupbill.repository;

//package com.example.whitefox.pickupbill.repository;

import com.example.whitefox.pickupbill.entity.PickupBillPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PickupBillPhotoRepository
        extends JpaRepository<PickupBillPhoto, UUID> {

    List<PickupBillPhoto> findByPickupBillId(UUID pickupBillId);
}
