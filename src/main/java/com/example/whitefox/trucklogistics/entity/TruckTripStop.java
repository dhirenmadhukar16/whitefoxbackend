package com.example.whitefox.trucklogistics.entity;

import com.example.whitefox.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "truck_trip_stops")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TruckTripStop {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private TruckTrip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    /**
     * STORE
     * HQ
     */
    private String stopType;

    private Integer stopSequence;

    private Double latitude;

    private Double longitude;

    private LocalDateTime expectedArrivalTime;

    private LocalDateTime actualArrivalTime;

    private LocalDateTime departureTime;

    private Integer garmentsLoaded;

    private Integer garmentsUnloaded;

    private Double weightLoaded;

    private Double weightUnloaded;

    /**
     * PENDING
     * REACHED
     * COMPLETED
     * SKIPPED
     */
    private String status;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();

        if (status == null) {
            status = "PENDING";
        }

        if (garmentsLoaded == null) {
            garmentsLoaded = 0;
        }

        if (garmentsUnloaded == null) {
            garmentsUnloaded = 0;
        }

        if (weightLoaded == null) {
            weightLoaded = 0.0;
        }

        if (weightUnloaded == null) {
            weightUnloaded = 0.0;
        }
    }
}