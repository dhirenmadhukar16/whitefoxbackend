package com.example.whitefox.notifications.service;

import com.example.whitefox.DeliveryOtp.dto.GenerateOtpResponse;
import com.example.whitefox.DeliveryOtp.dto.VerifyDeliveryOtpRequest;
import com.example.whitefox.DeliveryOtp.entity.DeliveryOtp;
import com.example.whitefox.DeliveryOtp.repository.DeliveryOtpRepository;
import com.example.whitefox.DeliveryOtp.service.DeliveryOtpService;
import com.example.whitefox.notifications.service.SmsService;
import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.enums.OrderStatus;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class DeliveryOtpServiceImpl implements DeliveryOtpService {

    private final DeliveryOtpRepository deliveryOtpRepository;
    private final LaundryOrderRepository laundryOrderRepository;
    private final SmsService smsService;

    @Override
    public GenerateOtpResponse generateOtp(UUID orderId) {

        LaundryOrder order = laundryOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        String phone = order.getCustomer().getPhone();

        if (phone == null || phone.isBlank()) {
            throw new RuntimeException("Customer phone number not found");
        }

        String otp = String.valueOf(
                ThreadLocalRandom.current().nextInt(100000, 1000000)
        );

        DeliveryOtp deliveryOtp = DeliveryOtp.builder()
                .order(order)
                .otp(otp)
                .verified(false)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        deliveryOtpRepository.save(deliveryOtp);

        smsService.sendOtp(phone, otp);


        GenerateOtpResponse response = new GenerateOtpResponse();
        response.setOtp(null);
        return response;



    }

    @Override
    public void verifyOtp(UUID orderId, VerifyDeliveryOtpRequest request) {

        DeliveryOtp latestOtp = deliveryOtpRepository
                .findTopByOrderIdOrderByCreatedAtDesc(orderId)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (Boolean.TRUE.equals(latestOtp.getVerified())) {
            throw new RuntimeException("OTP already verified");
        }

        if (latestOtp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        if (!latestOtp.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        LaundryOrder order = laundryOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        latestOtp.setVerified(true);
        deliveryOtpRepository.save(latestOtp);

        order.setStatus(OrderStatus.DELIVERED);
        laundryOrderRepository.save(order);
    }
}