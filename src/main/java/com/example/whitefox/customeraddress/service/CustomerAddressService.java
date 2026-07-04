package com.example.whitefox.customeraddress.service;


import com.example.whitefox.customeraddress.dto.CreateCustomerAddressRequest;
import com.example.whitefox.customeraddress.dto.CustomerAddressResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerAddressService {

    CustomerAddressResponse createAddress(CreateCustomerAddressRequest request);

    List<CustomerAddressResponse> getAddressesByCustomer(UUID customerId);

    CustomerAddressResponse getAddress(UUID addressId);

    CustomerAddressResponse updateAddress(
            UUID addressId,
            CreateCustomerAddressRequest request
    );

    void deleteAddress(UUID addressId);
}
