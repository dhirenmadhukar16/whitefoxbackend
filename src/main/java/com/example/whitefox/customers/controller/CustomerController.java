package com.example.whitefox.customers.controller;
//package com.example.whitefox.customer.controller;

import com.example.whitefox.customers.dto.CreateCustomerRequest;
import com.example.whitefox.customers.dto.CustomerResponse;
import com.example.whitefox.customers.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public CustomerResponse createCustomer(
            @RequestBody CreateCustomerRequest request) {

        return customerService.createCustomer(request);
    }

    @GetMapping
    public List<CustomerResponse> getAllCustomers() {

        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public CustomerResponse getCustomer(
            @PathVariable UUID id) {

        return customerService.getCustomer(id);
    }

    @PutMapping("/{id}")
    public CustomerResponse updateCustomer(
            @PathVariable UUID id,
            @RequestBody CreateCustomerRequest request) {

        return customerService.updateCustomer(
                id,
                request);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateCustomer(
            @PathVariable UUID id) {

        customerService.deactivateCustomer(id);

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/phone/{phone}")
    public CustomerResponse getCustomerByPhone(@PathVariable String phone) {
        return customerService.getCustomerByPhone(phone);
    }
}