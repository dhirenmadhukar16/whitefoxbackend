package com.example.whitefox.customerbooking.service;



import com.example.whitefox.customerbooking.dto.*;

import java.util.List;
import java.util.UUID;

public interface CustomerBookingService {

    CustomerBookingResponse createBooking(CreateCustomerBookingRequest request);

    List<CustomerBookingResponse> getBookingsByCustomer(UUID customerId);

    List<CustomerBookingResponse> getBookingsByStore(UUID storeId);

    CustomerBookingResponse getBooking(UUID bookingId);

    CustomerBookingResponse assignStore(UUID bookingId, AssignStoreRequest request);

    CustomerBookingResponse assignRider(UUID bookingId, AssignRiderRequest request);

    CustomerBookingResponse markRiderOnTheWay(UUID bookingId);

    CustomerBookingResponse markRiderReached(UUID bookingId);

    CustomerBookingResponse markPickupBillCreated(UUID bookingId);

    CustomerBookingResponse cancelBooking(UUID bookingId);
    CustomerBookingResponse rescheduleBooking(
            UUID bookingId,
            RescheduleBookingRequest request
    );
    List<CustomerBookingResponse> getBookingsByRider(UUID riderId);
}
