package com.example.whitefox.payment.service;

import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.enums.PaymentStatus;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import com.example.whitefox.payment.dto.*;
import com.example.whitefox.payment.entity.Payment;
import com.example.whitefox.payment.enums.PaymentMode;
import com.example.whitefox.payment.enums.PaymentTransactionStatus;
import com.example.whitefox.payment.repository.PaymentRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final LaundryOrderRepository orderRepository;

    @Value("${razorpay.key_id}")
    private String razorpayKeyId;

    @Value("${razorpay.key_secret}")
    private String razorpayKeySecret;

    @Override
    public RazorpayCreateOrderResponse createRazorpayOrder(
            RazorpayCreateOrderRequest request
    ) {
        try {
            LaundryOrder order = orderRepository.findById(request.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            int amountInPaise = (int) Math.round(request.getAmount() * 100);

            if ("YOUR_KEY_ID".equals(razorpayKeyId)) {
                return RazorpayCreateOrderResponse.builder()
                        .orderId(order.getId())
                        .razorpayOrderId("order_dummy_" + System.currentTimeMillis())
                        .keyId(razorpayKeyId)
                        .amount(request.getAmount())
                        .amountInPaise(amountInPaise)
                        .currency("INR")
                        .build();
            }

            RazorpayClient razorpayClient =
                    new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            JSONObject options = new JSONObject();
            options.put("amount", amountInPaise);
            options.put("currency", "INR");
            options.put("receipt", "WF-" + order.getOrderNumber());
            options.put("payment_capture", 1);

            com.razorpay.Order razorpayOrder =
                    razorpayClient.orders.create(options);

            return RazorpayCreateOrderResponse.builder()
                    .orderId(order.getId())
                    .razorpayOrderId(razorpayOrder.get("id"))
                    .keyId(razorpayKeyId)
                    .amount(request.getAmount())
                    .amountInPaise(amountInPaise)
                    .currency("INR")
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to create Razorpay order: " + e.getMessage()
            );
        }
    }

    @Override
    public PaymentResponse verifyRazorpayPayment(
            RazorpayVerifyPaymentRequest request
    ) {
        try {
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", request.getRazorpayOrderId());
            attributes.put("razorpay_payment_id", request.getRazorpayPaymentId());
            attributes.put("razorpay_signature", request.getRazorpaySignature());

            boolean valid = Utils.verifyPaymentSignature(
                    attributes,
                    razorpayKeySecret
            );

            if (!valid) {
                throw new RuntimeException("Invalid Razorpay signature");
            }

            LaundryOrder order = orderRepository.findById(request.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            Payment payment = Payment.builder()
                    .order(order)
                    .store(order.getStore())
                    .amount(request.getAmount())
                    .paymentMode(PaymentMode.ONLINE)
                    .transactionReference(request.getRazorpayPaymentId())
                    .remarks("Razorpay payment verified. Mode: " + request.getPaymentMode())
                    .status(PaymentTransactionStatus.SUCCESS)
                    .build();

            Payment saved = paymentRepository.save(payment);

            applyPaymentToOrder(order, request.getAmount(), request.getPaymentMode());

            return map(saved);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Payment verification failed: " + e.getMessage()
            );
        }
    }

    @Override
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        LaundryOrder order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = Payment.builder()
                .order(order)
                .store(order.getStore())
                .amount(request.getAmount())
                .paymentMode(request.getPaymentMode())
                .transactionReference(request.getTransactionReference())
                .remarks(request.getRemarks())
                .status(PaymentTransactionStatus.SUCCESS)
                .build();

        Payment saved = paymentRepository.save(payment);

        applyPaymentToOrder(
                order,
                request.getAmount(),
                request.getPaymentMode() != null
                        ? request.getPaymentMode().name()
                        : null
        );

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
    public List<PaymentResponse> getPaymentsByOrder(UUID orderId) {
        return paymentRepository.findByOrderId(orderId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public PaymentResponse getPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return map(payment);
    }

    @Override
    public PaymentResponse updatePaymentStatus(
            UUID paymentId,
            UpdatePaymentStatusRequest request
    ) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(request.getStatus());

        Payment saved = paymentRepository.save(payment);

        if (request.getStatus() == PaymentTransactionStatus.SUCCESS) {
            LaundryOrder order = payment.getOrder();

            recalculateOrderPayment(order);
        }

        return map(saved);
    }

    @Override
    public PaymentSummaryResponse getPaymentSummary() {
        List<Payment> payments = paymentRepository.findAll();

        double totalCollected = payments.stream()
                .filter(p -> p.getStatus() == PaymentTransactionStatus.SUCCESS)
                .mapToDouble(Payment::getAmount)
                .sum();

        double cashCollected = payments.stream()
                .filter(p -> p.getStatus() == PaymentTransactionStatus.SUCCESS)
                .filter(p -> p.getPaymentMode() == PaymentMode.CASH)
                .mapToDouble(Payment::getAmount)
                .sum();

        double upiCollected = payments.stream()
                .filter(p -> p.getStatus() == PaymentTransactionStatus.SUCCESS)
                .filter(p -> p.getPaymentMode() == PaymentMode.UPI)
                .mapToDouble(Payment::getAmount)
                .sum();

        double cardCollected = payments.stream()
                .filter(p -> p.getStatus() == PaymentTransactionStatus.SUCCESS)
                .filter(p -> p.getPaymentMode() == PaymentMode.CARD)
                .mapToDouble(Payment::getAmount)
                .sum();

        double onlineCollected = payments.stream()
                .filter(p -> p.getStatus() == PaymentTransactionStatus.SUCCESS)
                .filter(p -> p.getPaymentMode() == PaymentMode.ONLINE)
                .mapToDouble(Payment::getAmount)
                .sum();

        return PaymentSummaryResponse.builder()
                .totalTransactions((long) payments.size())
                .totalCollected(totalCollected)
                .cashCollected(cashCollected)
                .upiCollected(upiCollected)
                .cardCollected(cardCollected)
                .onlineCollected(onlineCollected)
                .build();
    }

    private void applyPaymentToOrder(
            LaundryOrder order,
            Double amount,
            String paymentMode
    ) {
        if (amount == null || amount <= 0) {
            throw new RuntimeException("Invalid payment amount");
        }

        recalculateOrderPayment(order);
    }

    private void recalculateOrderPayment(LaundryOrder order) {
        double total = order.getTotalAmount() != null
                ? order.getTotalAmount()
                : 0.0;

        double paid = paymentRepository.findByOrderId(order.getId())
                .stream()
                .filter(p -> p.getStatus() == PaymentTransactionStatus.SUCCESS)
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                .sum();

        double finalPaid = Math.min(paid, total);
        double remaining = Math.max(total - finalPaid, 0.0);

        order.setPaidAmount(finalPaid);
        order.setRemainingAmount(remaining);

        if (finalPaid <= 0) {
            order.setPaymentStatus(PaymentStatus.UNPAID);
        } else if (remaining <= 0) {
            order.setPaymentStatus(PaymentStatus.PAID);
        } else {
            order.setPaymentStatus(PaymentStatus.PARTIAL);
        }

        orderRepository.save(order);
    }

    private PaymentResponse map(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .orderNumber(payment.getOrder().getOrderNumber())
                .amount(payment.getAmount())
                .paymentMode(payment.getPaymentMode())
                .status(payment.getStatus())
                .transactionReference(payment.getTransactionReference())
                .remarks(payment.getRemarks())
                .paidAt(payment.getPaidAt())
                .build();
    }
    @Override
    public void markCodPending(UUID orderId) {
        LaundryOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setPaymentStatus(PaymentStatus.COD_PENDING);
        orderRepository.save(order);
    }
}