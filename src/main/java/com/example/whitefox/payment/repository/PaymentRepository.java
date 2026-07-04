package com.example.whitefox.payment.repository;

import com.example.whitefox.payment.entity.Payment;
import com.example.whitefox.payment.enums.PaymentTransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findByOrderId(UUID orderId);

    List<Payment> findByStoreId(UUID storeId);

    List<Payment> findByStoreIdAndStatus(
            UUID storeId,
            PaymentTransactionStatus status
    );

    boolean existsByTransactionReference(String transactionReference);

    @Query("""
           SELECT COALESCE(SUM(p.amount), 0)
           FROM Payment p
           WHERE p.store.id = :storeId
           AND p.status = :status
           """)
    Double sumAmountByStoreIdAndStatus(
            @Param("storeId") UUID storeId,
            @Param("status") PaymentTransactionStatus status
    );
}