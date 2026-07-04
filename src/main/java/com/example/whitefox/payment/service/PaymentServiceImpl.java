package com.example.whitefox.payment.service;



import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.enums.PaymentStatus;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import com.example.whitefox.payment.dto.*;
import com.example.whitefox.payment.entity.Payment;
import com.example.whitefox.payment.enums.PaymentMode;
import com.example.whitefox.payment.enums.PaymentTransactionStatus;
import com.example.whitefox.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final LaundryOrderRepository orderRepository;

    @Override
    public PaymentResponse createPayment(
            CreatePaymentRequest request
    ) {

        LaundryOrder order = orderRepository.findById(
                        request.getOrderId())
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        Payment payment = Payment.builder()
                .order(order)
                .amount(request.getAmount())
                .paymentMode(request.getPaymentMode())
                .transactionReference(
                        request.getTransactionReference())
                .remarks(request.getRemarks())
                .status(PaymentTransactionStatus.SUCCESS)
                .build();

        Payment saved = paymentRepository.save(payment);

        order.setPaymentStatus(PaymentStatus.PAID);
        orderRepository.save(order);

        return map(saved);
    }

    @Override
    public List<PaymentResponse> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<PaymentResponse> getPaymentsByOrder(
            UUID orderId
    ) {

        return paymentRepository.findByOrderId(orderId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public PaymentResponse getPayment(UUID paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        return map(payment);
    }

    @Override
    public PaymentResponse updatePaymentStatus(
            UUID paymentId,
            UpdatePaymentStatusRequest request
    ) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        payment.setStatus(request.getStatus());

        Payment saved = paymentRepository.save(payment);

        if (request.getStatus()
                == PaymentTransactionStatus.SUCCESS) {

            LaundryOrder order = payment.getOrder();

            order.setPaymentStatus(PaymentStatus.PAID);

            orderRepository.save(order);
        }

        return map(saved);
    }

    @Override
    public PaymentSummaryResponse getPaymentSummary() {

        List<Payment> payments =
                paymentRepository.findAll();

        double totalCollected =
                payments.stream()
                        .filter(p ->
                                p.getStatus() ==
                                        PaymentTransactionStatus.SUCCESS)
                        .mapToDouble(Payment::getAmount)
                        .sum();

        double cashCollected =
                payments.stream()
                        .filter(p ->
                                p.getStatus() ==
                                        PaymentTransactionStatus.SUCCESS)
                        .filter(p ->
                                p.getPaymentMode() ==
                                        PaymentMode.CASH)
                        .mapToDouble(Payment::getAmount)
                        .sum();

        double upiCollected =
                payments.stream()
                        .filter(p ->
                                p.getStatus() ==
                                        PaymentTransactionStatus.SUCCESS)
                        .filter(p ->
                                p.getPaymentMode() ==
                                        PaymentMode.UPI)
                        .mapToDouble(Payment::getAmount)
                        .sum();

        double cardCollected =
                payments.stream()
                        .filter(p ->
                                p.getStatus() ==
                                        PaymentTransactionStatus.SUCCESS)
                        .filter(p ->
                                p.getPaymentMode() ==
                                        PaymentMode.CARD)
                        .mapToDouble(Payment::getAmount)
                        .sum();

        double onlineCollected =
                payments.stream()
                        .filter(p ->
                                p.getStatus() ==
                                        PaymentTransactionStatus.SUCCESS)
                        .filter(p ->
                                p.getPaymentMode() ==
                                        PaymentMode.ONLINE)
                        .mapToDouble(Payment::getAmount)
                        .sum();

        return PaymentSummaryResponse.builder()
                .totalTransactions(
                        (long) payments.size())
                .totalCollected(totalCollected)
                .cashCollected(cashCollected)
                .upiCollected(upiCollected)
                .cardCollected(cardCollected)
                .onlineCollected(onlineCollected)
                .build();
    }

    private PaymentResponse map(Payment payment) {

        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .orderNumber(
                        payment.getOrder().getOrderNumber())
                .amount(payment.getAmount())
                .paymentMode(payment.getPaymentMode())
                .status(payment.getStatus())
                .transactionReference(
                        payment.getTransactionReference())
                .remarks(payment.getRemarks())
                .paidAt(payment.getPaidAt())
                .build();
    }
}