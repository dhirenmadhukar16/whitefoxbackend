package com.example.whitefox.assignment.entity;



import com.example.whitefox.assignment.enums.AssignmentStatus;
import com.example.whitefox.assignment.enums.AssignmentType;
import com.example.whitefox.assignment.enums.DeliveryMode;
import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.riders.entity.Rider;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private LaundryOrder order;

    @ManyToOne
    @JoinColumn(name = "rider_id", nullable = false)
    private Rider rider;

    @Enumerated(EnumType.STRING)
    private AssignmentType type;

    @Enumerated(EnumType.STRING)
    private AssignmentStatus status;

    @Enumerated(EnumType.STRING)
    private DeliveryMode deliveryMode;

    private String pickupAddress;

    private String dropAddress;

    private String remarks;

    private LocalDateTime assignedAt;

    private LocalDateTime completedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        status = AssignmentStatus.ASSIGNED;
        assignedAt = LocalDateTime.now();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
