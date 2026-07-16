package com.example.whitefox.pickupbill.service;



import com.example.whitefox.orders.dto.OrderResponse;
import com.example.whitefox.pickupbill.dto.CreatePickupBillRequest;
import com.example.whitefox.pickupbill.dto.PickupBillResponse;
import com.example.whitefox.pickupbill.dto.RejectPickupBillRequest;

import java.util.List;
import java.util.UUID;

public interface PickupBillService {

    PickupBillResponse createPickupBill(CreatePickupBillRequest request);

    List<PickupBillResponse> getAllPickupBills();

    PickupBillResponse getPickupBillById(UUID id);

    PickupBillResponse verifyPickupBill(UUID id);

    PickupBillResponse rejectPickupBill(UUID id, RejectPickupBillRequest request);

    OrderResponse convertToOrder(UUID pickupBillId);
    PickupBillResponse submitForVerification(UUID pickupBillId);

    PickupBillResponse collectClothes(UUID pickupBillId);

    PickupBillResponse dropAtStore(
            UUID pickupBillId,
            UUID dropStoreId,
            Double dropLatitude,
            Double dropLongitude
    );

    PickupBillResponse receiveByStore(UUID pickupBillId);
}
