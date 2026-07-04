package com.example.whitefox.pickupbill.repository;



import com.example.whitefox.pickupbill.entity.PickupBillItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PickupBillItemRepository
        extends JpaRepository<PickupBillItem, UUID> {

    List<PickupBillItem> findByPickupBillId(UUID pickupBillId);

}
