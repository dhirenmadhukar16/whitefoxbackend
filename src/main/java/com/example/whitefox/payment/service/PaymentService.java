package com.example.whitefox.payment.service;

import com.example.whitefox.payment.dto.*;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    PaymentResponse createPayment(CreatePaymentRequest request);

    List<PaymentResponse> getAllPayments();

    List<PaymentResponse> getPaymentsByOrder(UUID orderId);

    PaymentResponse getPayment(UUID paymentId);

    PaymentResponse updatePaymentStatus(
            UUID paymentId,
            UpdatePaymentStatusRequest request
    );

    PaymentSummaryResponse getPaymentSummary();
}
