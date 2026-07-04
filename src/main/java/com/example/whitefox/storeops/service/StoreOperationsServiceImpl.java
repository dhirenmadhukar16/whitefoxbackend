package com.example.whitefox.storeops.service;

import com.example.whitefox.customerupdates.dto.CreateCustomerUpdateRequest;
import com.example.whitefox.customerupdates.enums.CustomerUpdateType;
import com.example.whitefox.customerupdates.service.CustomerUpdateService;
import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.enums.OrderStatus;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import com.example.whitefox.pickupbill.dto.PickupBillResponse;
import com.example.whitefox.pickupbill.entity.PickupBill;
import com.example.whitefox.pickupbill.enums.PickupBillStatus;
import com.example.whitefox.pickupbill.repository.PickupBillItemRepository;
import com.example.whitefox.pickupbill.repository.PickupBillRepository;
import com.example.whitefox.store.entity.Store;
import com.example.whitefox.store.repository.StoreRepository;
import com.example.whitefox.storeemployee.enums.StoreEmployeeStatus;
import com.example.whitefox.storeemployee.repository.StoreEmployeeRepository;
import com.example.whitefox.storeops.dto.*;
import com.example.whitefox.tracking.entity.Garment;
import com.example.whitefox.tracking.repository.GarmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreOperationsServiceImpl implements StoreOperationsService {

    private final StoreRepository storeRepository;
    private final LaundryOrderRepository orderRepository;
    private final PickupBillRepository pickupBillRepository;
    private final PickupBillItemRepository pickupBillItemRepository;
    private final GarmentRepository garmentRepository;
    private final StoreEmployeeRepository employeeRepository;
    private final CustomerUpdateService customerUpdateService;

    @Override
    public StoreDashboardResponse getDashboard(UUID storeId) {

        Store store = getStore(storeId);

        List<LaundryOrder> orders = orderRepository.findAll()
                .stream()
                .filter(o -> o.getStore().getId().equals(storeId))
                .toList();

        List<PickupBill> pickupBills = pickupBillRepository.findAll()
                .stream()
                .filter(p -> p.getStore().getId().equals(storeId))
                .toList();

        List<Garment> garments = garmentRepository.findAll()
                .stream()
                .filter(g -> g.getOrder().getStore().getId().equals(storeId))
                .toList();

        double revenue = orders.stream()
                .filter(o -> o.getTotalAmount() != null)
                .mapToDouble(LaundryOrder::getTotalAmount)
                .sum();

        return StoreDashboardResponse.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .totalOrders((long) orders.size())
                .activeOrders(orders.stream()
                        .filter(o -> o.getStatus() != OrderStatus.DELIVERED)
                        .count())
                .readyOrders(orders.stream()
                        .filter(o -> o.getStatus() == OrderStatus.READY_FOR_CUSTOMER_PICKUP)
                        .count())
                .deliveredOrders(orders.stream()
                        .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                        .count())
                .pendingPickupBills(pickupBills.stream()
                        .filter(p -> p.getStatus() == PickupBillStatus.SENT_TO_STORE)
                        .count())
                .totalGarments((long) garments.size())
                .garmentsAtStore(garments.stream()
                        .filter(g -> g.getStatus().name().contains("STORE"))
                        .count())
                .garmentsReady(garments.stream()
                        .filter(g -> g.getStatus().name().contains("READY"))
                        .count())
                .totalEmployees((long) employeeRepository.findByStoreId(storeId).size())
                .activeEmployees((long) employeeRepository
                        .findByStoreIdAndStatus(storeId, StoreEmployeeStatus.ACTIVE)
                        .size())
                .totalRevenue(revenue)
                .build();
    }

    @Override
    public List<StoreOrderSummaryResponse> getOrdersByStore(UUID storeId) {
        return orderRepository.findAll()
                .stream()
                .filter(o -> o.getStore().getId().equals(storeId))
                .map(this::mapOrder)
                .toList();
    }

    @Override
    public List<StoreGarmentResponse> getGarmentsByStore(UUID storeId) {
        return garmentRepository.findAll()
                .stream()
                .filter(g -> g.getOrder().getStore().getId().equals(storeId))
                .map(this::mapGarment)
                .toList();
    }

    @Override
    public Object getPickupBillsByStore(UUID storeId) {
        return pickupBillRepository.findAll()
                .stream()
                .filter(p -> p.getStore().getId().equals(storeId))
                .map(this::mapPickupBill)
                .toList();
    }

    @Override
    public StoreOrderSummaryResponse markReceivedFromHq(UUID orderId) {
        LaundryOrder order = getOrder(orderId);
        order.setStatus(OrderStatus.RECEIVED_AT_STORE_AFTER_PROCESSING);

        LaundryOrder saved = orderRepository.save(order);

        createOrderUpdate(
                saved,
                CustomerUpdateType.RECEIVED_AT_STORE,
                "Received At Store",
                "Your clothes have been received back at the store."
        );

        return mapOrder(saved);
    }

    @Override
    public StoreOrderSummaryResponse markReadyForCustomerPickup(UUID orderId) {
        LaundryOrder order = getOrder(orderId);
        order.setStatus(OrderStatus.READY_FOR_CUSTOMER_PICKUP);

        LaundryOrder saved = orderRepository.save(order);

        createOrderUpdate(
                saved,
                CustomerUpdateType.READY_FOR_PICKUP,
                "Ready For Pickup",
                "Your clothes are ready for pickup or delivery."
        );

        return mapOrder(saved);
    }

    @Override
    public StoreOrderSummaryResponse markOutForDelivery(UUID orderId) {
        LaundryOrder order = getOrder(orderId);
        order.setStatus(OrderStatus.OUT_FOR_DELIVERY);

        LaundryOrder saved = orderRepository.save(order);

        createOrderUpdate(
                saved,
                CustomerUpdateType.OUT_FOR_DELIVERY,
                "Out For Delivery",
                "Your clothes are out for delivery."
        );

        return mapOrder(saved);
    }

    @Override
    public StoreOrderSummaryResponse markDelivered(UUID orderId) {
        LaundryOrder order = getOrder(orderId);
        order.setStatus(OrderStatus.DELIVERED);

        LaundryOrder saved = orderRepository.save(order);

        createOrderUpdate(
                saved,
                CustomerUpdateType.DELIVERED,
                "Delivered",
                "Your order has been delivered successfully."
        );

        return mapOrder(saved);
    }

    private Store getStore(UUID storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));
    }

    private LaundryOrder getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    private void createOrderUpdate(
            LaundryOrder order,
            CustomerUpdateType type,
            String title,
            String description
    ) {
        CreateCustomerUpdateRequest request = new CreateCustomerUpdateRequest();

        request.setCustomerId(order.getCustomer().getId());
        request.setOrderId(order.getId());
        request.setUpdateType(type);
        request.setTitle(title);
        request.setDescription(description);

        customerUpdateService.createUpdate(request);
    }

    private StoreOrderSummaryResponse mapOrder(LaundryOrder order) {
        return StoreOrderSummaryResponse.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .customerId(order.getCustomer().getId())
                .customerName(order.getCustomer().getName())
                .customerPhone(order.getCustomer().getPhone())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .totalAmount(order.getTotalAmount())
                .build();
    }

    private StoreGarmentResponse mapGarment(Garment garment) {
        return StoreGarmentResponse.builder()
                .garmentId(garment.getId())
                .orderId(garment.getOrder().getId())
                .orderNumber(garment.getOrder().getOrderNumber())
                .itemName(garment.getItemName())
                .serviceType(garment.getServiceType())
                .storeQrCode(garment.getStoreQrCode())
                .status(garment.getStatus())
                .build();
    }

    private PickupBillResponse mapPickupBill(PickupBill bill) {
        return PickupBillResponse.builder()
                .id(bill.getId())
                .billNumber(bill.getBillNumber())
                .customerId(bill.getCustomer().getId())
                .customerName(bill.getCustomer().getName())
                .riderId(bill.getRider().getId())
                .riderName(bill.getRider().getName())
                .storeId(bill.getStore().getId())
                .storeName(bill.getStore().getName())
                .pickupAddress(bill.getPickupAddress())
                .subtotal(bill.getSubtotal())
                .gst(bill.getGst())
                .totalAmount(bill.getTotalAmount())
                .status(bill.getStatus())
                .items(List.of())
                .build();
    }
}