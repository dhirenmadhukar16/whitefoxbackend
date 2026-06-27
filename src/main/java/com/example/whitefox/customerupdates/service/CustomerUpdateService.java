package com.example.whitefox.customerupdates.service;



import com.example.whitefox.customerupdates.dto.*;

import java.util.List;
import java.util.UUID;

public interface CustomerUpdateService {

    CustomerUpdateResponse createUpdate(CreateCustomerUpdateRequest request);

    List<CustomerUpdateResponse> getUpdatesByCustomer(UUID customerId);

    List<CustomerUpdateResponse> getTimelineByOrder(UUID orderId);

    CustomerUpdateResponse markAsRead(UUID updateId);
}