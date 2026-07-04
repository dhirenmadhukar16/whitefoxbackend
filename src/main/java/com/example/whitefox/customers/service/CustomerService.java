package com.example.whitefox.customers.service;



import com.example.whitefox.customers.dto.CreateCustomerRequest;
import com.example.whitefox.customers.dto.CustomerResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    CustomerResponse createCustomer(
            CreateCustomerRequest request);

    List<CustomerResponse> getAllCustomers();

    CustomerResponse getCustomer(UUID id);

    CustomerResponse updateCustomer(
            UUID id,
            CreateCustomerRequest request);

    void deactivateCustomer(UUID id);
}
