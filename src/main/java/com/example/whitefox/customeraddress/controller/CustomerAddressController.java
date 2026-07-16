package com.example.whitefox.customeraddress.controller;



import com.example.whitefox.customeraddress.dto.CreateCustomerAddressRequest;
import com.example.whitefox.customeraddress.dto.CustomerAddressResponse;
import com.example.whitefox.customeraddress.service.CustomerAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer-addresses")
@RequiredArgsConstructor
public class CustomerAddressController {

    private final CustomerAddressService addressService;

    @PostMapping
    public CustomerAddressResponse createAddress(
            @RequestBody CreateCustomerAddressRequest request
    ) {
        return addressService.createAddress(request);
    }

    @GetMapping("/customer/{customerId}")
    public List<CustomerAddressResponse> getAddressesByCustomer(
            @PathVariable UUID customerId
    ) {
        return addressService.getAddressesByCustomer(customerId);
    }

    @GetMapping("/{addressId}")
    public CustomerAddressResponse getAddress(
            @PathVariable UUID addressId
    ) {
        return addressService.getAddress(addressId);
    }

    @PutMapping("/{addressId}")
    public CustomerAddressResponse updateAddress(
            @PathVariable UUID addressId,
            @RequestBody CreateCustomerAddressRequest request
    ) {
        return addressService.updateAddress(addressId, request);
    }

    @DeleteMapping("/{addressId}")
    public void deleteAddress(
            @PathVariable UUID addressId
    ) {
        addressService.deleteAddress(addressId);
    }
}
