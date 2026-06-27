package com.example.whitefox.customerapp.dto;

import com.example.whitefox.customerupdates.dto.CustomerUpdateResponse;
import com.example.whitefox.orders.dto.OrderResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CustomerOrderDetailsResponse {

    private OrderResponse order;

    private List<CustomerUpdateResponse> timeline;
}