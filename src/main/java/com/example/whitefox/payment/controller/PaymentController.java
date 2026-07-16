package com.example.whitefox.payment.controller;

import com.example.whitefox.payment.dto.*;
import com.example.whitefox.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponse createPayment(@RequestBody CreatePaymentRequest request) {
        return paymentService.createPayment(request);
    }

    @GetMapping
    public List<PaymentResponse> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{paymentId}")
    public PaymentResponse getPayment(@PathVariable UUID paymentId) {
        return paymentService.getPayment(paymentId);
    }

    @GetMapping("/orders/{orderId}")
    public List<PaymentResponse> getPaymentsByOrder(@PathVariable UUID orderId) {
        return paymentService.getPaymentsByOrder(orderId);
    }

    @PatchMapping("/orders/{orderId}/cod")
    public void markCodPending(@PathVariable UUID orderId) {
        paymentService.markCodPending(orderId);
    }

    @PatchMapping("/{paymentId}/status")
    public PaymentResponse updatePaymentStatus(
            @PathVariable UUID paymentId,
            @RequestBody UpdatePaymentStatusRequest request
    ) {
        return paymentService.updatePaymentStatus(paymentId, request);
    }

    @GetMapping("/summary")
    public PaymentSummaryResponse getPaymentSummary() {
        return paymentService.getPaymentSummary();
    }

    @PostMapping("/razorpay/create-order")
    public RazorpayCreateOrderResponse createRazorpayOrder(
            @RequestBody RazorpayCreateOrderRequest request
    ) {
        return paymentService.createRazorpayOrder(request);
    }

    @PostMapping("/razorpay/verify")
    public PaymentResponse verifyRazorpayPayment(
            @RequestBody RazorpayVerifyPaymentRequest request
    ) {
        return paymentService.verifyRazorpayPayment(request);
    }

    @PostMapping("/razorpay/create-booking-order")
    public RazorpayCreateOrderResponse createRazorpayBookingOrder(
            @RequestBody RazorpayCreateBookingOrderRequest request
    ) {
        return paymentService.createRazorpayBookingOrder(request);
    }

    @PostMapping("/razorpay/verify-booking")
    public com.example.whitefox.customerbooking.dto.CustomerBookingResponse verifyRazorpayBookingPayment(
            @RequestBody RazorpayVerifyBookingPaymentRequest request
    ) {
        return paymentService.verifyRazorpayBookingPayment(request);
    }
}