package com.example.whitefox.pickupbill.repository;



import com.example.whitefox.pickupbill.entity.PickupBill;
import com.example.whitefox.pickupbill.enums.PickupBillStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PickupBillRepository
        extends JpaRepository<PickupBill, UUID> {

    List<PickupBill> findByStatus(PickupBillStatus status);

}
