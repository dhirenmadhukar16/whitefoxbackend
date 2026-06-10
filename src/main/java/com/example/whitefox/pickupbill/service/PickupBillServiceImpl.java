package com.example.whitefox.pickupbill.service;

import com.example.whitefox.customers.entity.Customer;
import com.example.whitefox.customers.repository.CustomerRepository;
import com.example.whitefox.orders.dto.OrderItemResponse;
import com.example.whitefox.orders.dto.OrderResponse;
import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.entity.OrderItem;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import com.example.whitefox.orders.repository.OrderItemRepository;
import com.example.whitefox.pickupbill.dto.*;
import com.example.whitefox.pickupbill.entity.PickupBill;
import com.example.whitefox.pickupbill.entity.PickupBillItem;
import com.example.whitefox.pickupbill.enums.PickupBillStatus;
import com.example.whitefox.pickupbill.repository.PickupBillItemRepository;
import com.example.whitefox.pickupbill.repository.PickupBillRepository;
import com.example.whitefox.riders.entity.Rider;
import com.example.whitefox.riders.repository.RiderRepository;
import com.example.whitefox.store.entity.Store;
import com.example.whitefox.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PickupBillServiceImpl implements PickupBillService {

    private final PickupBillRepository pickupBillRepository;
    private final PickupBillItemRepository pickupBillItemRepository;
    private final CustomerRepository customerRepository;
    private final RiderRepository riderRepository;
    private final StoreRepository storeRepository;
    private final LaundryOrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public PickupBillResponse createPickupBill(CreatePickupBillRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Rider rider = riderRepository.findById(request.getRiderId())
                .orElseThrow(() -> new RuntimeException("Rider not found"));

        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found"));

        PickupBill pickupBill = PickupBill.builder()
                .customer(customer)
                .rider(rider)
                .store(store)
                .pickupAddress(request.getPickupAddress())
                .status(PickupBillStatus.CREATED_BY_RIDER)
                .build();

        pickupBillRepository.save(pickupBill);

        double subtotal = 0.0;

        for (CreatePickupBillItemRequest itemRequest : request.getItems()) {

            double totalPrice = itemRequest.getUnitPrice() * itemRequest.getQuantity();

            PickupBillItem item = PickupBillItem.builder()
                    .pickupBill(pickupBill)
                    .itemName(itemRequest.getItemName())
                    .serviceType(itemRequest.getServiceType())
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(itemRequest.getUnitPrice())
                    .totalPrice(totalPrice)
                    .conditionNote(itemRequest.getConditionNote())
                    .photoUrl(itemRequest.getPhotoUrl())
                    .build();

            pickupBillItemRepository.save(item);

            subtotal += totalPrice;
        }

        double gst = subtotal * 0.18;
        double totalAmount = subtotal + gst;

        pickupBill.setSubtotal(subtotal);
        pickupBill.setGst(gst);
        pickupBill.setTotalAmount(totalAmount);
        pickupBill.setStatus(PickupBillStatus.SENT_TO_STORE);

        PickupBill saved = pickupBillRepository.save(pickupBill);

        return mapPickupBill(saved);
    }

    @Override
    public List<PickupBillResponse> getAllPickupBills() {
        return pickupBillRepository.findAll()
                .stream()
                .map(this::mapPickupBill)
                .toList();
    }

    @Override
    public PickupBillResponse getPickupBillById(UUID id) {

        PickupBill pickupBill = pickupBillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pickup bill not found"));

        return mapPickupBill(pickupBill);
    }

    @Override
    public PickupBillResponse verifyPickupBill(UUID id) {

        PickupBill pickupBill = pickupBillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pickup bill not found"));

        pickupBill.setStatus(PickupBillStatus.VERIFIED_BY_STORE);

        return mapPickupBill(pickupBillRepository.save(pickupBill));
    }

    @Override
    @Transactional
    public OrderResponse convertToOrder(UUID pickupBillId) {

        PickupBill pickupBill = pickupBillRepository.findById(pickupBillId)
                .orElseThrow(() -> new RuntimeException("Pickup bill not found"));

        if (pickupBill.getStatus() != PickupBillStatus.VERIFIED_BY_STORE) {
            throw new RuntimeException("Pickup bill must be verified before converting to order");
        }

        LaundryOrder order = LaundryOrder.builder()
                .customer(pickupBill.getCustomer())
                .store(pickupBill.getStore())
                .orderNumber(generateOrderNumber())
                .subtotal(pickupBill.getSubtotal())
                .gst(pickupBill.getGst())
                .totalAmount(pickupBill.getTotalAmount())
                .build();

        orderRepository.save(order);

        List<PickupBillItem> pickupItems =
                pickupBillItemRepository.findByPickupBillId(pickupBill.getId());

        for (PickupBillItem pickupItem : pickupItems) {

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .serviceType(pickupItem.getServiceType())
                    .itemName(pickupItem.getItemName())
                    .quantity(pickupItem.getQuantity())
                    .unitPrice(pickupItem.getUnitPrice())
                    .totalPrice(pickupItem.getTotalPrice())
                    .build();

            orderItemRepository.save(orderItem);
        }

        pickupBill.setStatus(PickupBillStatus.CONVERTED_TO_ORDER);
        pickupBillRepository.save(pickupBill);

        return mapOrder(order);
    }

    private PickupBillResponse mapPickupBill(PickupBill pickupBill) {

        List<PickupBillItemResponse> items =
                pickupBillItemRepository.findByPickupBillId(pickupBill.getId())
                        .stream()
                        .map(item ->
                                PickupBillItemResponse.builder()
                                        .itemName(item.getItemName())
                                        .serviceType(item.getServiceType())
                                        .quantity(item.getQuantity())
                                        .unitPrice(item.getUnitPrice())
                                        .totalPrice(item.getTotalPrice())
                                        .conditionNote(item.getConditionNote())
                                        .photoUrl(item.getPhotoUrl())
                                        .build()
                        )
                        .toList();

        return PickupBillResponse.builder()
                .id(pickupBill.getId())
                .billNumber(pickupBill.getBillNumber())
                .customerId(pickupBill.getCustomer().getId())
                .customerName(pickupBill.getCustomer().getName())
                .riderId(pickupBill.getRider().getId())
                .riderName(pickupBill.getRider().getName())
                .storeId(pickupBill.getStore().getId())
                .storeName(pickupBill.getStore().getName())
                .pickupAddress(pickupBill.getPickupAddress())
                .subtotal(pickupBill.getSubtotal())
                .gst(pickupBill.getGst())
                .totalAmount(pickupBill.getTotalAmount())
                .status(pickupBill.getStatus())
                .items(items)
                .build();
    }

    private OrderResponse mapOrder(LaundryOrder order) {

        List<OrderItemResponse> items =
                orderItemRepository.findByOrderId(order.getId())
                        .stream()
                        .map(item ->
                                OrderItemResponse.builder()
                                        .serviceType(item.getServiceType())
                                        .itemName(item.getItemName())
                                        .quantity(item.getQuantity())
                                        .unitPrice(item.getUnitPrice())
                                        .totalPrice(item.getTotalPrice())
                                        .build()
                        )
                        .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .customerId(order.getCustomer().getId())
                .customerName(order.getCustomer().getName())
                .storeId(order.getStore().getId())
                .storeName(order.getStore().getName())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .subtotal(order.getSubtotal())
                .gst(order.getGst())
                .totalAmount(order.getTotalAmount())
                .items(items)
                .build();
    }

    private String generateOrderNumber() {
        return "WF-" + System.currentTimeMillis();
    }
}