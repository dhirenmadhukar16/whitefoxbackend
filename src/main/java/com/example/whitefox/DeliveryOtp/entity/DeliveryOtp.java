package com.example.whitefox.DeliveryOtp.entity;

import com.example.whitefox.common.entity.BaseEntity;
import com.example.whitefox.orders.entity.LaundryOrder;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_otps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryOtp extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private LaundryOrder order;

    private String otp;

    private Boolean verified;

    private LocalDateTime expiresAt;
}