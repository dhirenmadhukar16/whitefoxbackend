package com.example.whitefox.pickupbill.entity;



import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "pickup_bill_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PickupBillItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "pickup_bill_id")
    private PickupBill pickupBill;

    private String itemName;

    private String serviceType;

    private Integer quantity;

    private Double unitPrice;

    private Double totalPrice;

    private String conditionNote;

    private String photoUrl;
}
