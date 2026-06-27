package com.example.whitefox.customerapp.dto;



import com.example.whitefox.customerbooking.dto.CustomerBookingResponse;
import com.example.whitefox.customerupdates.dto.CustomerUpdateResponse;
import com.example.whitefox.orders.dto.OrderResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CustomerDashboardResponse {

    private List<CustomerBookingResponse> activeBookings;

    private List<OrderResponse> activeOrders;

    private List<OrderResponse> recentOrders;

    private List<CustomerUpdateResponse> latestUpdates;
}