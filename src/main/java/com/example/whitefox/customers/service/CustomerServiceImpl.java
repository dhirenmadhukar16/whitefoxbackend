package com.example.whitefox.customers.service;



import com.example.whitefox.customers.dto.CreateCustomerRequest;
import com.example.whitefox.customers.dto.CustomerResponse;
import com.example.whitefox.customers.entity.Customer;
import com.example.whitefox.customers.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl
        implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponse createCustomer(
            CreateCustomerRequest request) {

        Customer customer = Customer.builder()
                .customerCode(
                        "CUST-" +
                                System.currentTimeMillis())
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .city(request.getCity())
                .build();

        customerRepository.save(customer);

        return map(customer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {

        return customerRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public CustomerResponse getCustomer(UUID id) {

        Customer customer =
                customerRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found"));

        return map(customer);
    }

    @Override
    public CustomerResponse updateCustomer(
            UUID id,
            CreateCustomerRequest request) {

        Customer customer =
                customerRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found"));

        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setCity(request.getCity());

        Customer saved =
                customerRepository.save(customer);

        return map(saved);
    }

    @Override
    public void deactivateCustomer(UUID id) {

        Customer customer =
                customerRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found"));

        customer.setActive(false);

        customerRepository.save(customer);
    }

    private CustomerResponse map(Customer customer) {

        return CustomerResponse.builder()
                .id(customer.getId())
                .customerCode(customer.getCustomerCode())
                .name(customer.getName())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .city(customer.getCity())
                .active(customer.getActive())
                .loyaltyPoints(customer.getLoyaltyPoints() != null ? customer.getLoyaltyPoints() : 0)
                .build();
    }
    @Override
    public CustomerResponse getCustomerByPhone(String phone) {
        Customer customer = customerRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return map(customer);
    }
}