package com.example.whitefox.Riderdelivery.service;

import com.example.whitefox.customerupdates.dto.CreateCustomerUpdateRequest;
import com.example.whitefox.customerupdates.enums.CustomerUpdateType;
import com.example.whitefox.customerupdates.service.CustomerUpdateService;
import com.example.whitefox.orders.dto.OrderItemResponse;
import com.example.whitefox.orders.dto.OrderResponse;
import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.entity.OrderItem;
import com.example.whitefox.orders.enums.OrderStatus;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import com.example.whitefox.orders.repository.OrderItemRepository;
import com.example.whitefox.riders.entity.Rider;
import com.example.whitefox.riders.repository.RiderRepository;
import com.example.whitefox.tracking.enums.GarmentStatus;
import com.example.whitefox.tracking.repository.GarmentRepository;
import com.example.whitefox.Riderdelivery.dto.VerifyOtpRequest;
import com.example.whitefox.customers.repository.CustomerRepository;
import com.example.whitefox.customers.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RiderDeliveryServiceImpl implements RiderDeliveryService {

    private final LaundryOrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final RiderRepository riderRepository;
    private final CustomerUpdateService customerUpdateService;
    private final CustomerRepository customerRepository;

    @Override
    public List<OrderResponse> getDeliveryOrders(UUID riderId) {
        return orderRepository.findByDeliveryRiderId(riderId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public OrderResponse assignDelivery(UUID orderId, UUID riderId) {
        LaundryOrder order = getOrder(orderId);

        Rider rider = riderRepository.findById(riderId)
                .orElseThrow(() -> new RuntimeException("Rider not found"));

        order.setDeliveryRider(rider);
        order.setStatus(OrderStatus.ASSIGNED_FOR_DELIVERY);

        LaundryOrder saved = orderRepository.save(order);

        createOrderUpdate(
                saved,
                CustomerUpdateType.OUT_FOR_DELIVERY,
                "Delivery Rider Assigned",
                rider.getName() + " has been assigned for your delivery."
        );

        return map(saved);
    }

    @Override
    public OrderResponse markOutForDelivery(UUID orderId) {
        LaundryOrder order = getOrder(orderId);

        order.setStatus(OrderStatus.OUT_FOR_DELIVERY);

        LaundryOrder saved = orderRepository.save(order);

        createOrderUpdate(
                saved,
                CustomerUpdateType.OUT_FOR_DELIVERY,
                "Out For Delivery",
                "Your order is out for delivery."
        );

        return map(saved);
    }

    @Override
    public OrderResponse markDelivered(UUID orderId) {
        LaundryOrder order = getOrder(orderId);

        order.setStatus(OrderStatus.DELIVERED);

        if (order.getCustomer() != null && order.getTotalAmount() != null) {
            Customer customer = order.getCustomer();
            int pointsToAdd = (int) (order.getTotalAmount() / 10);
            customer.setLoyaltyPoints(
                    (customer.getLoyaltyPoints() != null ? customer.getLoyaltyPoints() : 0) + pointsToAdd
            );
            customerRepository.save(customer);
        }

        LaundryOrder saved = orderRepository.save(order);

        createOrderUpdate(
                saved,
                CustomerUpdateType.DELIVERED,
                "Delivered",
                "Your order has been delivered successfully."
        );

        return map(saved);
    }

    @Override
    public OrderResponse markDeliveryFailed(UUID orderId) {
        LaundryOrder order = getOrder(orderId);

        order.setStatus(OrderStatus.DELIVERY_FAILED);

        LaundryOrder saved = orderRepository.save(order);

        createOrderUpdate(
                saved,
                CustomerUpdateType.CANCELLED,
                "Delivery Failed",
                "Delivery attempt failed. Our team will contact you shortly."
        );

        return map(saved);
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

    private OrderResponse map(LaundryOrder order) {
        List<OrderItemResponse> items =
                orderItemRepository.findByOrderId(order.getId())
                        .stream()
                        .map(this::mapItem)
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

    private OrderItemResponse mapItem(OrderItem item) {
        return OrderItemResponse.builder()
                .serviceType(item.getServiceType())
                .itemName(item.getItemName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .totalPrice(item.getTotalPrice())
                .build();
    }

    @Override
    public OrderResponse verifyOtp(UUID orderId, VerifyOtpRequest request) {
        LaundryOrder order = getOrder(orderId);

        if (order.getDeliveryOtp() != null && !order.getDeliveryOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        // If OTP matches, check if rider collected cash
        if (request.isCashCollected()) {
            double amountToCollect = order.getRemainingAmount() != null ? order.getRemainingAmount() : 
                                     (order.getTotalAmount() != null ? order.getTotalAmount() : 0.0);
            order.setPaidAmount((order.getPaidAmount() != null ? order.getPaidAmount() : 0.0) + amountToCollect);
            order.setRemainingAmount(0.0);
            order.setPaymentStatus(com.example.whitefox.orders.enums.PaymentStatus.CASH_WITH_RIDER);
        }

        return markDelivered(orderId);
    }
}