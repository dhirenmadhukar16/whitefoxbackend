package com.example.whitefox.adminpanel.service;

import com.example.whitefox.adminpanel.dto.CustomerCrmDto;
import com.example.whitefox.customerbooking.entity.CustomerBooking;

import java.util.List;
import java.util.UUID;

public interface AdminCustomerService {
    List<CustomerCrmDto> getAllCustomersCrmData();
    List<CustomerBooking> getCustomerOrders(UUID customerId);
}
