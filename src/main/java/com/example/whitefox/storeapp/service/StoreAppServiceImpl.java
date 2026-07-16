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
import com.example.whitefox.riders.enums.RiderStatus;
import com.example.whitefox.riders.repository.RiderRepository;
import com.example.whitefox.auth.entity.User;
import com.example.whitefox.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.whitefox.storeemployee.dto.CreateStoreEmployeeRequest;
import com.example.whitefox.storeemployee.dto.StoreEmployeeResponse;
import com.example.whitefox.storeemployee.dto.UpdateStoreEmployeeRequest;
import com.example.whitefox.store.entity.Store;
import com.example.whitefox.storeemployee.entity.StoreEmployee;
import com.example.whitefox.storeemployee.enums.StoreEmployeeStatus;
import com.example.whitefox.storeemployee.repository.StoreEmployeeRepository;
import com.example.whitefox.store.repository.StoreRepository;
import com.example.whitefox.store.repository.StoreRepository;
import com.example.whitefox.storeemployee.enums.StoreEmployeeRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreAppServiceImpl implements StoreAppService {

    private final StoreRepository storeRepository;
    private final StoreEmployeeRepository storeEmployeeRepository;
    private final RiderRepository riderRepository;
    private final UserRepository userRepository;
    private final CustomerBookingRepository customerBookingRepository;
    private final LaundryOrderRepository orderRepository;
    private final CustomerBookingService customerBookingService;
    private final PasswordEncoder passwordEncoder;

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

        StoreEmployee savedEmployee = storeEmployeeRepository.save(employee);

        if (request.getRole() == StoreEmployeeRole.RIDER) {
            Optional<Rider> riderOpt = riderRepository.findByPhone(request.getPhone());
            if (riderOpt.isEmpty()) {
                Rider rider = Rider.builder()
                        .name(request.getName())
                        .phone(request.getPhone())
                        .email(request.getEmail() != null ? request.getEmail() : "rider_" + request.getPhone() + "@whitefox.local")
                        .store(store)
                        .status(RiderStatus.AVAILABLE)
                        .active(true)
                        .build();
                riderRepository.save(rider);
            } else {
                Rider existingRider = riderOpt.get();
                existingRider.setStore(store);
                existingRider.setStatus(RiderStatus.AVAILABLE);
                riderRepository.save(existingRider);
            }

            Optional<User> userOpt = userRepository.findByPhone(request.getPhone());
            if (userOpt.isEmpty()) {
                User user = User.builder()
                        .firstName(request.getName())
                        .lastName("Rider")
                        .phone(request.getPhone())
                        .email(request.getEmail() != null ? request.getEmail() : "rider_" + request.getPhone() + "@whitefox.local")
                        .password(passwordEncoder.encode(java.util.UUID.randomUUID().toString()))
                        .role("RIDER")
                        .storeId(store.getId())
                        .active(true)
                        .build();
                userRepository.save(user);
            } else {
                User existingUser = userOpt.get();
                existingUser.setStoreId(store.getId());
                userRepository.save(existingUser);
            }
        }

        return mapEmployee(savedEmployee);
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

    @Override
    public void resetRiderPassword(UUID storeId, UUID riderId, String newPassword) {
        Rider rider = riderRepository.findById(riderId)
                .orElseThrow(() -> new RuntimeException("Rider not found"));

        if (rider.getStore() == null || !rider.getStore().getId().equals(storeId)) {
            throw new RuntimeException("Rider does not belong to this store");
        }

        User user = userRepository.findByPhone(rider.getPhone())
                .orElseThrow(() -> new RuntimeException("Rider user account not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
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