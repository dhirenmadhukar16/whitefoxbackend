package com.example.whitefox.trucklogistics.service;

import com.example.whitefox.tracking.enums.GarmentStatus;
import com.example.whitefox.trucklogistics.dto.*;

import java.util.List;
import java.util.UUID;

public interface TruckLogisticsService {

    TruckResponse createTruck(CreateTruckRequest request);

    List<TruckResponse> getAllTrucks();

    TruckResponse getTruck(UUID truckId);

    TruckTripResponse createTrip(CreateTruckTripRequest request);

    TruckTripResponse startTrip(UUID tripId);

    TruckTripResponse completeTrip(UUID tripId);

    List<TruckTripResponse> getTripsByTruck(UUID truckId);

    TruckStopResponse addStop(CreateTruckStopRequest request);

    List<TruckStopResponse> getStopsByTrip(UUID tripId);

    TruckStopResponse markStopReached(UUID stopId);

    TruckStopResponse markStopCompleted(UUID stopId);

    ManifestResponse createManifest(CreateManifestRequest request);

    ManifestResponse addManifestItem(UUID manifestId, AddManifestItemRequest request);

    List<ManifestResponse> getManifestsByTrip(UUID tripId);

    ManifestResponse markManifestDeliveredToHq(UUID manifestId);

    ManifestResponse markManifestDeliveredToStore(UUID manifestId);

    TruckResponse updateTruckLocation(TruckLocationUpdateRequest request);
    List<TruckStopResponse> getIncomingStopsForStore(UUID storeId);
    TruckLiveLocationResponse getLiveLocation(UUID truckId);
    ManifestResponse deliverManifestItemsToStore(UUID manifestId, UUID storeId);
    List<TruckLiveLocationResponse> getAllLiveTrucks();
    List<ManifestItemResponse> getManifestItems(UUID manifestId);
    List<ManifestItemResponse> getManifestItemsByStore(
            UUID manifestId,
            UUID storeId
    );

}