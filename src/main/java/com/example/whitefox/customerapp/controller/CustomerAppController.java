package com.example.whitefox.customerapp.controller;

//package com.example.whitefox.customerapp.controller;

import com.example.whitefox.garments.dto.ServiceCatalogResponse;
import com.example.whitefox.garments.service.ServiceCatalogService;
import com.example.whitefox.orders.dto.OrderResponse;
import com.example.whitefox.orders.service.OrderService;
import com.example.whitefox.store.dto.StoreResponse;
import com.example.whitefox.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.whitefox.customers.dto.CreateCustomerRequest;
import com.example.whitefox.customers.dto.CustomerResponse;
import com.example.whitefox.customers.service.CustomerService;
import com.example.whitefox.customerapp.dto.CustomerOrderDetailsResponse;
import com.example.whitefox.customerupdates.dto.CustomerUpdateResponse;
import com.example.whitefox.customerupdates.service.CustomerUpdateService;
import com.example.whitefox.customerapp.dto.CustomerDashboardResponse;
import com.example.whitefox.customerbooking.dto.CustomerBookingResponse;
import com.example.whitefox.customerbooking.service.CustomerBookingService;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer-app")
@RequiredArgsConstructor
public class CustomerAppController {

    private final ServiceCatalogService serviceCatalogService;
    private final StoreService storeService;
    private final OrderService orderService;
    private final CustomerService customerService;
    private final CustomerUpdateService customerUpdateService;
    private final CustomerBookingService customerBookingService;
    @GetMapping("/services")
    public List<ServiceCatalogResponse> getServices() {
        return serviceCatalogService.getAll();
    }

    @GetMapping("/stores")
    public List<StoreResponse> getStores() {
        return storeService.getAllStores();
    }

    @GetMapping("/customers/{customerId}/orders")
    public List<OrderResponse> getCustomerOrders(
            @PathVariable UUID customerId
    ) {
        return orderService.getAllOrders()
                .stream()
                .filter(order -> order.getCustomerId().equals(customerId))
                .toList();
    }

    @GetMapping("/customers/{customerId}/orders/recent")
    public List<OrderResponse> getRecentOrders(
            @PathVariable UUID customerId
    ) {
        return orderService.getAllOrders()
                .stream()
                .filter(order -> order.getCustomerId().equals(customerId))
                .sorted(Comparator.comparing(OrderResponse::getOrderNumber).reversed())
                .limit(5)
                .toList();
    }

    @GetMapping("/customers/{customerId}/orders/history")
    public List<OrderResponse> getPastOrders(
            @PathVariable UUID customerId
    ) {
        return orderService.getAllOrders()
                .stream()
                .filter(order -> order.getCustomerId().equals(customerId))
                .toList();
    }

    @GetMapping("/stores/nearby")
    public List<StoreResponse> getNearbyStores(
            @RequestParam Double latitude,
            @RequestParam Double longitude
    ) {
        return storeService.getAllStores()
                .stream()
                .sorted(Comparator.comparingDouble(store ->
                        distance(
                                latitude,
                                longitude,
                                store.getLatitude(),
                                store.getLongitude()
                        )
                ))
                .limit(5)
                .toList();
    }

    @GetMapping("/stores/nearest")
    public StoreResponse getNearestStore(
            @RequestParam Double latitude,
            @RequestParam Double longitude
    ) {
        return storeService.getAllStores()
                .stream()
                .filter(store -> distance(latitude, longitude, store.getLatitude(), store.getLongitude()) <= 10.0)
                .min(Comparator.comparingDouble(store ->
                        distance(latitude, longitude, store.getLatitude(), store.getLongitude())
                ))
                .orElse(null);
    }

    private double distance(
            double lat1,
            double lon1,
            Double lat2,
            Double lon2
    ) {
        if (lat2 == null || lon2 == null) {
            return Double.MAX_VALUE;
        }

        double earthRadius = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2)
                        * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(
                Math.sqrt(a),
                Math.sqrt(1 - a)
        );

        return earthRadius * c;
    }
    @GetMapping("/customers/{customerId}/profile")
    public CustomerResponse getCustomerProfile(
            @PathVariable UUID customerId
    ) {
        return customerService.getCustomer(customerId);
    }

    @PutMapping("/customers/{customerId}/profile")
    public CustomerResponse updateCustomerProfile(
            @PathVariable UUID customerId,
            @RequestBody CreateCustomerRequest request
    ) {
        return customerService.updateCustomer(customerId, request);
    }
    @GetMapping("/orders/{orderId}")
    public CustomerOrderDetailsResponse getOrderDetails(
            @PathVariable UUID orderId
    ) {
        OrderResponse order = orderService.getOrder(orderId);

        List<CustomerUpdateResponse> timeline =
                customerUpdateService.getTimelineByOrder(orderId);

        return CustomerOrderDetailsResponse.builder()
                .order(order)
                .timeline(timeline)
                .build();
    }
    @GetMapping("/customers/{customerId}/dashboard")
    public CustomerDashboardResponse getCustomerDashboard(
            @PathVariable UUID customerId
    ) {
        List<CustomerBookingResponse> bookings =
                customerBookingService.getBookingsByCustomer(customerId);

        List<OrderResponse> orders = orderService.getAllOrders()
                .stream()
                .filter(order -> order.getCustomerId().equals(customerId))
                .toList();

        List<CustomerUpdateResponse> updates =
                customerUpdateService.getUpdatesByCustomer(customerId);

        return CustomerDashboardResponse.builder()
                .activeBookings(
                        bookings.stream()
                                .filter(b -> !b.getStatus().name().equals("CANCELLED")
                                        && !b.getStatus().name().equals("PICKUP_BILL_CREATED"))
                                .toList()
                )
                .activeOrders(
                        orders.stream()
                                .filter(o -> !o.getStatus().name().equals("DELIVERED")
                                        && !o.getStatus().name().equals("CANCELLED"))
                                .toList()
                )
                .recentOrders(
                        orders.stream()
                                .limit(5)
                                .toList()
                )
                .latestUpdates(
                        updates.stream()
                                .limit(5)
                                .toList()
                )
                .build();
    }
}