package com.example.whitefox.payment.dto;



import com.example.whitefox.payment.enums.PaymentTransactionStatus;
import lombok.Data;

@Data
public class UpdatePaymentStatusRequest {

    private PaymentTransactionStatus status;
}
