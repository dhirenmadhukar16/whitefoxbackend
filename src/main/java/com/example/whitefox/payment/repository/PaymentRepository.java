package com.example.whitefox.payment.repository;

import com.example.whitefox.payment.entity.Payment;
import com.example.whitefox.payment.enums.PaymentTransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository
        extends JpaRepository<Payment, UUID> {

    List<Payment> findByOrderId(UUID orderId);

    List<Payment> findByStatus(
            PaymentTransactionStatus status
    );
}
