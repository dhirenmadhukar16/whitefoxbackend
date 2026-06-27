package com.example.whitefox.storeapp.service;

import com.example.whitefox.customerbooking.dto.AssignRiderRequest;
import com.example.whitefox.customerbooking.dto.CustomerBookingResponse;
import com.example.whitefox.customerbooking.entity.CustomerBooking;
import com.example.whitefox.customerbooking.enums.CustomerBookingStatus;
import com.example.whitefox.customerbooking.repository.CustomerBookingRepository;
import com.example.whitefox.customerbooking.service.CustomerBookingService;
import com.example.whitefox.orders.dto.OrderResponse;
import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import com.example.whitefox.riders.dto.RiderResponse;
import com.example.whitefox.riders.entity.Rider;
import com.example.whitefox.riders.repository.RiderRepository;
import com.example.whitefox.storeemployee.dto.CreateStoreEmployeeRequest;
import com.example.whitefox.storeemployee.dto.StoreEmployeeResponse;
import com.example.whitefox.storeemployee.dto.UpdateStoreEmployeeRequest;
import com.example.whitefox.store.entity.Store;
import com.example.whitefox.storeemployee.entity.StoreEmployee;
import com.example.whitefox.storeemployee.enums.StoreEmployeeStatus;
import com.example.whitefox.storeemployee.repository.StoreEmployeeRepository;
import com.example.whitefox.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreAppServiceImpl implements StoreAppService {

    private final StoreRepository storeRepository;
    private final StoreEmployeeRepository storeEmployeeRepository;
    private final RiderRepository riderRepository;
    private final CustomerBookingRepository customerBookingRepository;
    private final LaundryOrderRepository orderRepository;
    private final CustomerBookingService customerBookingService;

    @Override
    public List<StoreEmployeeResponse> getEmployees(UUID storeId) {
        return storeEmployeeRepository.findByStoreId(storeId)
                .stream()
                .map(this::mapEmployee)
                .toList();
    }

    @Override
    public StoreEmployeeResponse addEmployee(UUID storeId, CreateStoreEmployeeRequest request) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        StoreEmployee employee = StoreEmployee.builder()
                .store(store)
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .role(request.getRole())
                .status(StoreEmployeeStatus.ACTIVE)
                .active(true)
                .build();

        return mapEmployee(storeEmployeeRepository.save(employee));
    }

    @Override
    public StoreEmployeeResponse updateEmployee(UUID employeeId, UpdateStoreEmployeeRequest request) {
        StoreEmployee employee = storeEmployeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setName(request.getName());
        employee.setPhone(request.getPhone());
        employee.setEmail(request.getEmail());
        employee.setRole(request.getRole());

        return mapEmployee(storeEmployeeRepository.save(employee));
    }

    @Override
    public void deactivateEmployee(UUID employeeId) {
        StoreEmployee employee = storeEmployeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setStatus(StoreEmployeeStatus.INACTIVE);
        employee.setActive(false);

        storeEmployeeRepository.save(employee);
    }

    @Override
    public List<RiderResponse> getRiders() {
        return riderRepository.findAll()
                .stream()
                .map(this::mapRider)
                .toList();
    }

    @Override
    public CustomerBookingResponse assignRiderToBooking(UUID bookingId, UUID riderId) {
        CustomerBooking booking = customerBookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Rider rider = riderRepository.findById(riderId)
                .orElseThrow(() -> new RuntimeException("Rider not found"));
        AssignRiderRequest request = new AssignRiderRequest();
        request.setRiderId(riderId);

        return customerBookingService.assignRider(bookingId, request);

    }

    @Override
    public OrderResponse assignDeliveryRiderToOrder(UUID orderId, UUID riderId) {
        LaundryOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Rider rider = riderRepository.findById(riderId)
                .orElseThrow(() -> new RuntimeException("Rider not found"));

        order.setDeliveryRider(rider);

        return mapOrder(orderRepository.save(order));
    }

    private StoreEmployeeResponse mapEmployee(StoreEmployee employee) {
        return StoreEmployeeResponse.builder()
                .id(employee.getId())
                .storeId(employee.getStore().getId())
                .storeName(employee.getStore().getName())
                .name(employee.getName())
                .phone(employee.getPhone())
                .email(employee.getEmail())
                .role(employee.getRole())
                .status(employee.getStatus())
                .active(employee.getActive())
                .build();
    }

    private RiderResponse mapRider(Rider rider) {
        return RiderResponse.builder()
                .id(rider.getId())
                .riderCode(rider.getRiderCode())
                .name(rider.getName())
                .phone(rider.getPhone())
                .email(rider.getEmail())
                .vehicleNumber(rider.getVehicleNumber())
                .latitude(rider.getLatitude())
                .longitude(rider.getLongitude())
                .status(rider.getStatus())
                .active(rider.getActive())
                .build();
    }

    private OrderResponse mapOrder(LaundryOrder order) {
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
                .build();
    }
}