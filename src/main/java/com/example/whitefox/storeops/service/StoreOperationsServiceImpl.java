package com.example.whitefox.storeops.service;

import com.example.whitefox.customerupdates.dto.CreateCustomerUpdateRequest;
import com.example.whitefox.customerupdates.enums.CustomerUpdateType;
import com.example.whitefox.customerupdates.service.CustomerUpdateService;
import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.enums.OrderStatus;
import com.example.whitefox.orders.enums.PaymentStatus;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import com.example.whitefox.payment.repository.PaymentRepository;
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
import com.example.whitefox.customers.repository.CustomerRepository;
import com.example.whitefox.customers.entity.Customer;
import com.example.whitefox.riders.entity.Rider;
import com.example.whitefox.riders.enums.RiderStatus;
import com.example.whitefox.riders.repository.RiderRepository;
import com.example.whitefox.riders.dto.RiderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import com.example.whitefox.store.entity.StoreServicePricing;
import com.example.whitefox.store.repository.StoreServicePricingRepository;
import com.example.whitefox.garments.entity.ServiceCatalog;
import com.example.whitefox.garments.repository.ServiceCatalogRepository;
import java.util.stream.Collectors;

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
    private final StoreServicePricingRepository pricingRepository;
    private final ServiceCatalogRepository serviceCatalogRepository;
    private final CustomerRepository customerRepository;
    private final RiderRepository riderRepository;
    private final PaymentRepository paymentRepository;

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
        
        // Generate 4-digit OTP for self-pickup
        String otp = String.format("%04d", new java.util.Random().nextInt(10000));
        order.setDeliveryOtp(otp);

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
        
        // Generate 4-digit OTP
        String otp = String.format("%04d", new java.util.Random().nextInt(10000));
        order.setDeliveryOtp(otp);

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

        return mapOrder(saved);
    }

    @Override
    public StoreOrderSummaryResponse verifyPickupOtp(UUID orderId, String otp) {
        LaundryOrder order = getOrder(orderId);
        if (order.getStatus() != OrderStatus.READY_FOR_CUSTOMER_PICKUP) {
            throw new RuntimeException("Order is not ready for pickup.");
        }
        if (order.getDeliveryOtp() == null || !order.getDeliveryOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP.");
        }
        
        return markDelivered(orderId);
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
                .deliveryType(order.getDeliveryType() != null ? order.getDeliveryType().name() : null)
                .pickupType(order.getPickupType())
                .deliveryOtp(order.getDeliveryOtp())
                .pickupOtp(order.getPickupOtp())
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

    @Override
    public List<StoreServicePricingDto> getStorePricing(UUID storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        List<ServiceCatalog> allServices = serviceCatalogRepository.findAll();
        List<StoreServicePricing> customPrices = pricingRepository.findByStoreId(storeId);

        return allServices.stream().map(service -> {
            StoreServicePricing custom = customPrices.stream()
                    .filter(cp -> cp.getServiceCatalog().getId().equals(service.getId()))
                    .findFirst()
                    .orElse(null);

            return StoreServicePricingDto.builder()
                    .serviceId(service.getId())
                    .serviceType(service.getCategory() != null ? service.getCategory().getName() : null)
                    .itemName(service.getItemName())
                    .globalPrice(service.getPrice())
                    .storeCustomPrice(custom != null ? custom.getCustomPrice() : null)
                    .isOverridden(custom != null)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public StoreServicePricingDto setStorePricing(UUID storeId, SetStorePricingRequest request) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        ServiceCatalog service = serviceCatalogRepository.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        StoreServicePricing pricing = pricingRepository.findByStoreIdAndServiceCatalogId(storeId, service.getId())
                .orElse(StoreServicePricing.builder()
                        .store(store)
                        .serviceCatalog(service)
                        .build());

        pricing.setCustomPrice(request.getCustomPrice());
        pricingRepository.save(pricing);

        return StoreServicePricingDto.builder()
                .serviceId(service.getId())
                .serviceType(service.getCategory() != null ? service.getCategory().getName() : null)
                .itemName(service.getItemName())
                .globalPrice(service.getPrice())
                .storeCustomPrice(pricing.getCustomPrice())
                .isOverridden(true)
                .build();
    }

    @Override
    public StoreOrderSummaryResponse settleCash(UUID orderId) {
        LaundryOrder order = getOrder(orderId);
        if (order.getPaymentStatus() == PaymentStatus.CASH_WITH_RIDER || order.getPaymentStatus() == PaymentStatus.PENDING) {
            order.setPaymentStatus(PaymentStatus.PAID);
            order.setPaidAmount(order.getTotalAmount());
            order.setRemainingAmount(0.0);
            
            LaundryOrder saved = orderRepository.save(order);
            return mapOrder(saved);
        }
        throw new RuntimeException("Order is not eligible for cash settlement.");
    }

    @Override
    public StoreOrderSummaryResponse settleOnlinePayment(UUID orderId) {
        LaundryOrder order = getOrder(orderId);
        if (order.getPaymentStatus() == PaymentStatus.PENDING || order.getPaymentStatus() == PaymentStatus.CASH_WITH_RIDER) {
            order.setPaymentStatus(PaymentStatus.PAID);
            order.setPaidAmount(order.getTotalAmount());
            order.setRemainingAmount(0.0);
            
            // Generate a fake payment transaction to keep track
            com.example.whitefox.payment.entity.Payment payment = com.example.whitefox.payment.entity.Payment.builder()
                .order(order)
                .store(order.getStore())
                .amount(order.getTotalAmount())
                .paymentMode(com.example.whitefox.payment.enums.PaymentMode.UPI)
                .transactionReference("STORE_UPI_" + System.currentTimeMillis())
                .remarks("UPI Payment collected at Store")
                .status(com.example.whitefox.payment.enums.PaymentTransactionStatus.SUCCESS)
                .build();
            paymentRepository.save(payment);
            
            LaundryOrder saved = orderRepository.save(order);
            return mapOrder(saved);
        }
        throw new RuntimeException("Order is not eligible for online payment settlement.");
    }

    @Override
    public List<RiderResponse> getPendingRiders(UUID storeId) {
        return riderRepository.findAll().stream()
                .filter(r -> r.getStore() != null && r.getStore().getId().equals(storeId))
                .filter(r -> r.getStatus() == RiderStatus.PENDING_APPROVAL)
                .map(this::mapRider)
                .collect(Collectors.toList());
    }

    @Override
    public RiderResponse approveRider(UUID riderId) {
        Rider rider = riderRepository.findById(riderId)
                .orElseThrow(() -> new RuntimeException("Rider not found"));
        
        if (rider.getStatus() != RiderStatus.PENDING_APPROVAL) {
            throw new RuntimeException("Rider is not pending approval");
        }
        
        rider.setStatus(RiderStatus.AVAILABLE);
        Rider saved = riderRepository.save(rider);
        return mapRider(saved);
    }

    @Override
    public RiderResponse rejectRider(UUID riderId) {
        Rider rider = riderRepository.findById(riderId)
                .orElseThrow(() -> new RuntimeException("Rider not found"));
        
        if (rider.getStatus() != RiderStatus.PENDING_APPROVAL) {
            throw new RuntimeException("Rider is not pending approval");
        }
        
        rider.setStatus(RiderStatus.REJECTED);
        Rider saved = riderRepository.save(rider);
        return mapRider(saved);
    }

    private RiderResponse mapRider(Rider rider) {
        return RiderResponse.builder()
                .id(rider.getId())
                .riderCode(rider.getRiderCode())
                .name(rider.getName())
                .phone(rider.getPhone())
                .email(rider.getEmail())
                .whatsappNumber(rider.getWhatsappNumber())
                .vehicleNumber(rider.getVehicleNumber())
                .status(rider.getStatus())
                .active(rider.getActive())
                .storeId(rider.getStore() != null ? rider.getStore().getId() : null)
                .build();
    }
}