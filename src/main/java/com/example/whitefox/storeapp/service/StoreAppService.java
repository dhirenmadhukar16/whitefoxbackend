package com.example.whitefox.storeapp.service;

import com.example.whitefox.customerbooking.dto.CustomerBookingResponse;
import com.example.whitefox.riders.dto.RiderResponse;
import com.example.whitefox.storeemployee.dto.CreateStoreEmployeeRequest;
import com.example.whitefox.storeemployee.dto.StoreEmployeeResponse;
import com.example.whitefox.storeemployee.dto.UpdateStoreEmployeeRequest;
import com.example.whitefox.orders.dto.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface StoreAppService {

    List<StoreEmployeeResponse> getEmployees(UUID storeId);

    StoreEmployeeResponse addEmployee(UUID storeId, CreateStoreEmployeeRequest request);

    StoreEmployeeResponse updateEmployee(UUID employeeId, UpdateStoreEmployeeRequest request);

    void deactivateEmployee(UUID employeeId);

    List<RiderResponse> getRiders();

    CustomerBookingResponse assignRiderToBooking(UUID bookingId, UUID riderId);

    OrderResponse assignDeliveryRiderToOrder(UUID orderId, UUID riderId);

    void resetRiderPassword(UUID storeId, UUID riderId, String newPassword);
}