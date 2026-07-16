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
import com.example.whitefox.orders.enums.OrderStatus;
import com.example.whitefox.pickupbill.entity.PickupBill;
import com.example.whitefox.pickupbill.entity.PickupBillItem;
import com.example.whitefox.pickupbill.repository.PickupBillItemRepository;
import com.example.whitefox.payment.entity.Payment;
import com.example.whitefox.payment.enums.PaymentTransactionStatus;
import com.example.whitefox.payment.enums.PaymentMode;
import com.example.whitefox.payment.repository.PaymentRepository;
import com.example.whitefox.customeraddress.entity.CustomerAddress;
import com.example.whitefox.customeraddress.repository.CustomerAddressRepository;
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
    private final PickupBillItemRepository pickupBillItemRepository;
    private final PaymentRepository paymentRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final com.example.whitefox.tracking.repository.GarmentRepository garmentRepository;

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
                .deliveryType(request.getDeliveryType() != null && request.getDeliveryType().equals("SELF_PICKUP") ? com.example.whitefox.orders.enums.DeliveryType.SELF_PICKUP : com.example.whitefox.orders.enums.DeliveryType.RIDER_DELIVERY)
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
                    .serviceType(catalog.getCategory() != null ? catalog.getCategory().getName() : null)
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
    @Transactional
    public OrderResponse createOrderFromPickupBill(PickupBill pickupBill) {

        Store finalStore = pickupBill.getStore();

        com.example.whitefox.customerbooking.entity.CustomerBooking booking = pickupBill.getCustomerBooking();
        com.example.whitefox.orders.enums.DeliveryType deliveryType = com.example.whitefox.orders.enums.DeliveryType.RIDER_DELIVERY;
        double paidAmount = 0.0;
        
        if (booking != null) {
            if (booking.getDeliveryType() != null) {
                try {
                    deliveryType = com.example.whitefox.orders.enums.DeliveryType.valueOf(booking.getDeliveryType());
                } catch (Exception e) {}
            }
            if (booking.getAmountPaid() != null) {
                paidAmount = booking.getAmountPaid();
            }
        }

        LaundryOrder order = LaundryOrder.builder()
                .customer(pickupBill.getCustomer())
                .store(finalStore)
                .orderNumber(generateOrderNumber())
                .pickupDate(java.time.LocalDate.now())
                .deliveryDate(java.time.LocalDate.now().plusDays(2))
                .status(OrderStatus.RECEIVED_AT_STORE)
                .subtotal(pickupBill.getSubtotal())
                .gst(pickupBill.getGst())
                .totalAmount(pickupBill.getTotalAmount())
                .paidAmount(paidAmount)
                .remainingAmount(pickupBill.getTotalAmount() - paidAmount)
                .deliveryType(deliveryType)
                .paymentStatus(paidAmount >= pickupBill.getTotalAmount() ? com.example.whitefox.orders.enums.PaymentStatus.PAID : (paidAmount > 0 ? com.example.whitefox.orders.enums.PaymentStatus.PARTIAL : com.example.whitefox.orders.enums.PaymentStatus.UNPAID))
                .build();

        LaundryOrder savedOrder = orderRepository.save(order);

        if (paidAmount > 0) {
            Payment payment = Payment.builder()
                    .order(savedOrder)
                    .store(savedOrder.getStore())
                    .amount(paidAmount)
                    .paymentMode(booking != null && booking.getPaymentMode() != null ? PaymentMode.valueOf(booking.getPaymentMode()) : PaymentMode.ONLINE)
                    .status(PaymentTransactionStatus.SUCCESS)
                    .remarks("Advance Payment via Booking")
                    .paidAt(java.time.LocalDateTime.now())
                    .build();
            paymentRepository.save(payment);
        }

        List<PickupBillItem> pickupItems =
                pickupBillItemRepository.findByPickupBillId(pickupBill.getId());

        for (PickupBillItem pickupItem : pickupItems) {

            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .serviceType(pickupItem.getServiceType())
                    .itemName(pickupItem.getItemName())
                    .quantity(pickupItem.getQuantity())
                    .unitPrice(pickupItem.getUnitPrice())
                    .totalPrice(pickupItem.getTotalPrice())
                    .build();

            orderItemRepository.save(orderItem);
        }

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

        String deliveryAddress = null;
        List<CustomerAddress> addresses = customerAddressRepository.findByCustomerId(order.getCustomer().getId());
        if (!addresses.isEmpty()) {
            CustomerAddress defaultAddr = addresses.stream()
                    .filter(a -> Boolean.TRUE.equals(a.getDefaultAddress()))
                    .findFirst()
                    .orElse(addresses.get(0));
            deliveryAddress = defaultAddr.getAddressLine() + 
                              (defaultAddr.getLandmark() != null && !defaultAddr.getLandmark().trim().isEmpty() ? ", " + defaultAddr.getLandmark() : "") + 
                              (defaultAddr.getCity() != null ? ", " + defaultAddr.getCity() : "");
        } else if (order.getCustomer().getAddress() != null) {
            deliveryAddress = order.getCustomer().getAddress();
        }

        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .customerId(order.getCustomer().getId())
                .customerName(order.getCustomer().getName())
                .customerPhone(order.getCustomer().getPhone())
                .storeId(order.getStore().getId())
                .storeName(order.getStore().getName())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .subtotal(order.getSubtotal())
                .gst(order.getGst())
                .totalAmount(order.getTotalAmount())
                .deliveryType(order.getDeliveryType() != null ? order.getDeliveryType().name() : null)
                .deliveryAddress(deliveryAddress)
                .items(items)
                .build();
    }

    @Override
    @Transactional
    public OrderResponse changeDeliveryType(UUID orderId, String deliveryType) {
        LaundryOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        try {
            com.example.whitefox.orders.enums.DeliveryType newType = com.example.whitefox.orders.enums.DeliveryType.valueOf(deliveryType.toUpperCase());
            order.setDeliveryType(newType);
            
            if (newType == com.example.whitefox.orders.enums.DeliveryType.SELF_PICKUP) {
                if (order.getStatus() == OrderStatus.READY_FOR_DELIVERY) {
                    order.setStatus(OrderStatus.READY_FOR_CUSTOMER_PICKUP);
                    if (order.getDeliveryOtp() == null || order.getDeliveryOtp().isEmpty()) {
                        order.setDeliveryOtp(String.format("%04d", new java.util.Random().nextInt(10000)));
                    }
                    
                    List<com.example.whitefox.tracking.entity.Garment> garments = garmentRepository.findByOrderId(orderId);
                    for (com.example.whitefox.tracking.entity.Garment g : garments) {
                        if (g.getStatus() == com.example.whitefox.tracking.enums.GarmentStatus.READY_FOR_DELIVERY) {
                            g.setStatus(com.example.whitefox.tracking.enums.GarmentStatus.READY_FOR_CUSTOMER_PICKUP);
                            garmentRepository.save(g);
                        }
                    }
                }
            } else if (newType == com.example.whitefox.orders.enums.DeliveryType.RIDER_DELIVERY) {
                if (order.getStatus() == OrderStatus.READY_FOR_CUSTOMER_PICKUP) {
                    order.setStatus(OrderStatus.READY_FOR_DELIVERY);
                    
                    List<com.example.whitefox.tracking.entity.Garment> garments = garmentRepository.findByOrderId(orderId);
                    for (com.example.whitefox.tracking.entity.Garment g : garments) {
                        if (g.getStatus() == com.example.whitefox.tracking.enums.GarmentStatus.READY_FOR_CUSTOMER_PICKUP) {
                            g.setStatus(com.example.whitefox.tracking.enums.GarmentStatus.READY_FOR_DELIVERY);
                            garmentRepository.save(g);
                        }
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid delivery type");
        }
        
        LaundryOrder saved = orderRepository.save(order);
        return map(saved);
    }
}
