package com.example.whitefox.trucklogistics.entity;

import com.example.whitefox.trucklogistics.enums.TruckTripStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "truck_trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TruckTrip {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id")
    private Truck truck;

    @Column(nullable = false, unique = true)
    private String tripNumber;

    /**
     * MORNING
     * AFTERNOON
     * EVENING
     * NIGHT
     */
    private String shift;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private TruckTripStatus status;

    private Integer totalStores;

    private Integer completedStores;

    private Integer totalGarments;

    private Double totalWeight;

    private Boolean returnTrip;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();

        status = TruckTripStatus.TRIP_CREATED;

        completedStores = 0;

        totalGarments = 0;

        totalWeight = 0.0;

        returnTrip = false;
    }
}