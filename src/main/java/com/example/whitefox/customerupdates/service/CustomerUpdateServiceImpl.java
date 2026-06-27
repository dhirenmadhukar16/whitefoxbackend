package com.example.whitefox.customerupdates.service;


import com.example.whitefox.customerupdates.dto.CreateCustomerUpdateRequest;
import com.example.whitefox.customerupdates.dto.CustomerUpdateResponse;
import com.example.whitefox.customerupdates.entity.CustomerUpdate;
import com.example.whitefox.customerupdates.repository.CustomerUpdateRepository;
import com.example.whitefox.customers.entity.Customer;
import com.example.whitefox.customers.repository.CustomerRepository;
import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerUpdateServiceImpl implements CustomerUpdateService {

    private final CustomerUpdateRepository updateRepository;
    private final CustomerRepository customerRepository;
    private final LaundryOrderRepository orderRepository;

    @Override
    public CustomerUpdateResponse createUpdate(CreateCustomerUpdateRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        LaundryOrder order = null;

        if (request.getOrderId() != null) {
            order = orderRepository.findById(request.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
        }

        CustomerUpdate update = CustomerUpdate.builder()
                .customer(customer)
                .order(order)
                .updateType(request.getUpdateType())
                .title(request.getTitle())
                .description(request.getDescription())
                .readStatus(false)
                .build();

        return map(updateRepository.save(update));
    }

    @Override
    public List<CustomerUpdateResponse> getUpdatesByCustomer(UUID customerId) {
        return updateRepository.findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<CustomerUpdateResponse> getTimelineByOrder(UUID orderId) {
        return updateRepository.findByOrderIdOrderByCreatedAtAsc(orderId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public CustomerUpdateResponse markAsRead(UUID updateId) {
        CustomerUpdate update = updateRepository.findById(updateId)
                .orElseThrow(() -> new RuntimeException("Update not found"));

        update.setReadStatus(true);

        return map(updateRepository.save(update));
    }

    private CustomerUpdateResponse map(CustomerUpdate update) {
        return CustomerUpdateResponse.builder()
                .id(update.getId())
                .customerId(update.getCustomer().getId())
                .orderId(update.getOrder() != null ? update.getOrder().getId() : null)
                .updateType(update.getUpdateType())
                .title(update.getTitle())
                .description(update.getDescription())
                .readStatus(update.getReadStatus())
                .createdAt(update.getCreatedAt())
                .build();
    }
}