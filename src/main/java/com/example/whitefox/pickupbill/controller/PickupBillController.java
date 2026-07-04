package com.example.whitefox.pickupbill.controller;

import com.example.whitefox.orders.dto.OrderResponse;
import com.example.whitefox.pickupbill.dto.CreatePickupBillRequest;
import com.example.whitefox.pickupbill.dto.DropAtStoreRequest;
import com.example.whitefox.pickupbill.dto.PickupBillResponse;
import com.example.whitefox.pickupbill.service.PickupBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pickup-bills")
@RequiredArgsConstructor
public class PickupBillController {

    private final PickupBillService pickupBillService;

    @PostMapping
    public PickupBillResponse createPickupBill(
            @RequestBody CreatePickupBillRequest request
    ) {
        return pickupBillService.createPickupBill(request);
    }

    @GetMapping
    public List<PickupBillResponse> getAllPickupBills() {
        return pickupBillService.getAllPickupBills();
    }

    @GetMapping("/{id}")
    public PickupBillResponse getPickupBillById(
            @PathVariable UUID id
    ) {
        return pickupBillService.getPickupBillById(id);
    }

    @PatchMapping("/{id}/submit")
    public PickupBillResponse submitForVerification(
            @PathVariable UUID id
    ) {
        return pickupBillService.submitForVerification(id);
    }

    @PatchMapping("/{id}/verify")
    public PickupBillResponse verifyPickupBill(
            @PathVariable UUID id
    ) {
        return pickupBillService.verifyPickupBill(id);
    }

    @PatchMapping("/{id}/collect-clothes")
    public PickupBillResponse collectClothes(
            @PathVariable UUID id
    ) {
        return pickupBillService.collectClothes(id);
    }

    @PatchMapping("/{id}/drop-store")
    public PickupBillResponse dropAtStore(
            @PathVariable UUID id,
            @RequestBody DropAtStoreRequest request
    ) {
        return pickupBillService.dropAtStore(
                id,
                request.getDropStoreId(),
                request.getDropLatitude(),
                request.getDropLongitude()
        );
    }

    @PatchMapping("/{id}/receive-clothes")
    public PickupBillResponse receiveClothes(
            @PathVariable UUID id
    ) {
        return pickupBillService.receiveByStore(id);
    }

    @PostMapping("/{id}/convert-to-order")
    public OrderResponse convertToOrder(
            @PathVariable UUID id
    ) {
        return pickupBillService.convertToOrder(id);
    }

}