package com.example.whitefox.adminpanel.service;

import com.example.whitefox.adminpanel.dto.CustomerCrmDto;
import com.example.whitefox.customerbooking.entity.CustomerBooking;
import com.example.whitefox.customerbooking.enums.CustomerBookingStatus;
import com.example.whitefox.customerbooking.repository.CustomerBookingRepository;
import com.example.whitefox.customers.entity.Customer;
import com.example.whitefox.customers.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCustomerServiceImpl implements AdminCustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerBookingRepository customerBookingRepository;

    @Override
    public List<CustomerCrmDto> getAllCustomersCrmData() {
        List<Customer> customers = customerRepository.findAll();
        
        return customers.stream().map(customer -> {
            List<CustomerBooking> orders = customerBookingRepository.findByCustomerId(customer.getId());
            
            long totalOrders = orders.size();
            
            double lifetimeValue = orders.stream()
                .filter(o -> o.getStatus() == CustomerBookingStatus.PICKUP_BILL_CREATED)
                .mapToDouble(o -> o.getEstimatedAmount() != null ? o.getEstimatedAmount() : 0.0)
                .sum();
            
            long cancelledOrders = orders.stream()
                .filter(o -> o.getStatus() == CustomerBookingStatus.CANCELLED)
                .count();
                
            String riskScore = "Low";
            if (totalOrders > 0) {
                double cancelRate = (double) cancelledOrders / totalOrders;
                if (cancelRate > 0.5) riskScore = "High";
                else if (cancelRate > 0.2) riskScore = "Medium";
            }
            
            return CustomerCrmDto.builder()
                .id(customer.getId())
                .customerCode(customer.getCustomerCode())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .city(customer.getCity())
                .active(customer.getActive())
                .joinedAt(customer.getCreatedAt())
                .totalOrders(totalOrders)
                .lifetimeValue(lifetimeValue)
                .rewardPoints(customer.getLoyaltyPoints() != null ? customer.getLoyaltyPoints() : 0)
                .riskScore(riskScore)
                .build();
        }).collect(Collectors.toList());
    }

    @Override
    public List<CustomerBooking> getCustomerOrders(UUID customerId) {
        return customerBookingRepository.findByCustomerId(customerId);
    }
}
