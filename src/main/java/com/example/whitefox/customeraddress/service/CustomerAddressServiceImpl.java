package com.example.whitefox.customeraddress.service;



import com.example.whitefox.customeraddress.dto.CreateCustomerAddressRequest;
import com.example.whitefox.customeraddress.dto.CustomerAddressResponse;
import com.example.whitefox.customeraddress.entity.CustomerAddress;
import com.example.whitefox.customeraddress.repository.CustomerAddressRepository;
import com.example.whitefox.customers.entity.Customer;
import com.example.whitefox.customers.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerAddressServiceImpl implements CustomerAddressService {

    private final CustomerAddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    @Override
    public CustomerAddressResponse createAddress(CreateCustomerAddressRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        CustomerAddress address = CustomerAddress.builder()
                .customer(customer)
                .label(request.getLabel())
                .addressLine(request.getAddressLine())
                .landmark(request.getLandmark())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .defaultAddress(request.getDefaultAddress())
                .build();

        return map(addressRepository.save(address));
    }

    @Override
    public List<CustomerAddressResponse> getAddressesByCustomer(UUID customerId) {
        return addressRepository.findByCustomerId(customerId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public CustomerAddressResponse getAddress(UUID addressId) {
        return map(getAddressEntity(addressId));
    }

    @Override
    public CustomerAddressResponse updateAddress(
            UUID addressId,
            CreateCustomerAddressRequest request
    ) {
        CustomerAddress address = getAddressEntity(addressId);

        address.setLabel(request.getLabel());
        address.setAddressLine(request.getAddressLine());
        address.setLandmark(request.getLandmark());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setLatitude(request.getLatitude());
        address.setLongitude(request.getLongitude());
        address.setDefaultAddress(request.getDefaultAddress());

        return map(addressRepository.save(address));
    }

    @Override
    public void deleteAddress(UUID addressId) {
        CustomerAddress address = getAddressEntity(addressId);
        addressRepository.delete(address);
    }

    private CustomerAddress getAddressEntity(UUID addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
    }

    private CustomerAddressResponse map(CustomerAddress address) {
        return CustomerAddressResponse.builder()
                .id(address.getId())
                .customerId(address.getCustomer().getId())
                .customerName(address.getCustomer().getName())
                .label(address.getLabel())
                .addressLine(address.getAddressLine())
                .landmark(address.getLandmark())
                .city(address.getCity())
                .state(address.getState())
                .pincode(address.getPincode())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .defaultAddress(address.getDefaultAddress())
                .build();
    }
}