package com.example.whitefox.orders.service;



import com.example.whitefox.customers.entity.Customer;
import com.example.whitefox.customers.repository.CustomerRepository;
import com.example.whitefox.garments.entity.ServiceCatalog;
import com.example.whitefox.garments.repository.ServiceCatalogRepository;
import com.example.whitefox.orders.dto.CreateOrderRequest;
import com.example.whitefox.orders.dto.OrderItemRequest;
import com.example.whitefox.orders.dto.OrderItemResponse;
import com.example.whitefox.orders.dto.OrderResponse;
import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.entity.OrderItem;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import com.example.whitefox.orders.repository.OrderItemRepository;
import com.example.whitefox.store.entity.Store;
import com.example.whitefox.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final LaundryOrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final ServiceCatalogRepository serviceCatalogRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {

        Customer customer = customerRepository.findById(
                        request.getCustomerId()
                )
                .orElseThrow(() ->
                        new RuntimeException("Customer not found")
                );

        Store store = storeRepository.findById(
                        request.getStoreId()
                )
                .orElseThrow(() ->
                        new RuntimeException("Store not found")
                );

        LaundryOrder order = LaundryOrder.builder()
                .customer(customer)
                .store(store)
                .orderNumber(generateOrderNumber())
                .pickupDate(request.getPickupDate())
                .deliveryDate(request.getDeliveryDate())
                .build();

        orderRepository.save(order);

        double subtotal = 0.0;

        for (OrderItemRequest itemRequest : request.getItems()) {

            ServiceCatalog catalog =
                    serviceCatalogRepository.findById(
                                    itemRequest.getCatalogId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Catalog item not found"
                                    )
                            );

            double itemTotal =
                    catalog.getPrice()
                            * itemRequest.getQuantity();

            OrderItem item = OrderItem.builder()
                    .order(order)
                    .serviceCatalog(catalog)
                    .serviceType(catalog.getServiceType())
                    .itemName(catalog.getItemName())
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(catalog.getPrice())
                    .totalPrice(itemTotal)
                    .build();

            orderItemRepository.save(item);

            subtotal += itemTotal;
        }

        double gst = subtotal * 0.18;
        double totalAmount = subtotal + gst;

        order.setSubtotal(subtotal);
        order.setGst(gst);
        order.setTotalAmount(totalAmount);

        LaundryOrder savedOrder = orderRepository.save(order);

        return map(savedOrder);
    }

    @Override
    public OrderResponse getOrder(UUID orderId) {

        LaundryOrder order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found")
                );

        return map(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    private String generateOrderNumber() {

        return "WF-" + System.currentTimeMillis();
    }

    private OrderResponse map(LaundryOrder order) {

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
}
