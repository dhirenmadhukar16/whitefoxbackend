package com.example.whitefox.trucklogistics.controller;

import com.example.whitefox.adminpanel.dto.AdminTruckStatsResponse;
import com.example.whitefox.adminpanel.service.AdminPanelService;
import com.example.whitefox.trucklogistics.dto.*;
import com.example.whitefox.trucklogistics.service.TruckLogisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.whitefox.trucklogistics.repository.TruckRepository;
import com.example.whitefox.trucklogistics.entity.Truck;
import com.example.whitefox.common.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/truck-logistics")
@RequiredArgsConstructor
public class TruckLogisticsController {

    private final TruckLogisticsService truckLogisticsService;
    private final AdminPanelService adminPanelService;
    private final TruckRepository truckRepository;

    @GetMapping("/me")
    public TruckResponse getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        Truck truck = truckRepository.findAll().stream()
                .filter(t -> email.equals(t.getEmail()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Truck Driver not found"));
                
        return truckLogisticsService.getTruck(truck.getId());
    }


    @PostMapping("/trucks")
    public TruckResponse createTruck(
            @RequestBody CreateTruckRequest request
    ) {
        return truckLogisticsService.createTruck(request);
    }

    @GetMapping("/trucks")
    public List<TruckResponse> getAllTrucks() {
        return truckLogisticsService.getAllTrucks();
    }

    @GetMapping("/trucks/{truckId}")
    public TruckResponse getTruck(
            @PathVariable UUID truckId
    ) {
        return truckLogisticsService.getTruck(truckId);
    }

    @PostMapping("/trips")
    public TruckTripResponse createTrip(
            @RequestBody CreateTruckTripRequest request
    ) {
        return truckLogisticsService.createTrip(request);
    }

    @PatchMapping("/trips/{tripId}/start")
    public TruckTripResponse startTrip(
            @PathVariable UUID tripId
    ) {
        return truckLogisticsService.startTrip(tripId);
    }

    @PatchMapping("/trips/{tripId}/complete")
    public TruckTripResponse completeTrip(
            @PathVariable UUID tripId
    ) {
        return truckLogisticsService.completeTrip(tripId);
    }

    @GetMapping("/trucks/{truckId}/trips")
    public List<TruckTripResponse> getTripsByTruck(
            @PathVariable UUID truckId
    ) {
        return truckLogisticsService.getTripsByTruck(truckId);
    }

    @PostMapping("/stops")
    public TruckStopResponse addStop(
            @RequestBody CreateTruckStopRequest request
    ) {
        return truckLogisticsService.addStop(request);
    }

    @GetMapping("/trips/{tripId}/stops")
    public List<TruckStopResponse> getStopsByTrip(
            @PathVariable UUID tripId
    ) {
        return truckLogisticsService.getStopsByTrip(tripId);
    }

    @PatchMapping("/stops/{stopId}/reached")
    public TruckStopResponse markStopReached(
            @PathVariable UUID stopId
    ) {
        return truckLogisticsService.markStopReached(stopId);
    }

    @PatchMapping("/stops/{stopId}/completed")
    public TruckStopResponse markStopCompleted(
            @PathVariable UUID stopId
    ) {
        return truckLogisticsService.markStopCompleted(stopId);
    }

    @PostMapping("/manifests")
    public ManifestResponse createManifest(
            @RequestBody CreateManifestRequest request
    ) {
        return truckLogisticsService.createManifest(request);
    }

    @PostMapping("/manifests/{manifestId}/items")
    public ManifestResponse addManifestItem(
            @PathVariable UUID manifestId,
            @RequestBody AddManifestItemRequest request
    ) {
        return truckLogisticsService.addManifestItem(
                manifestId,
                request
        );
    }

    @GetMapping("/trips/{tripId}/manifests")
    public List<ManifestResponse> getManifestsByTrip(
            @PathVariable UUID tripId
    ) {
        return truckLogisticsService.getManifestsByTrip(tripId);
    }

    @PatchMapping("/manifests/{manifestId}/delivered-to-hq")
    public ManifestResponse markManifestDeliveredToHq(
            @PathVariable UUID manifestId
    ) {
        return truckLogisticsService.markManifestDeliveredToHq(manifestId);
    }

    @PatchMapping("/manifests/{manifestId}/delivered-to-store")
    public ManifestResponse markManifestDeliveredToStore(
            @PathVariable UUID manifestId
    ) {
        return truckLogisticsService.markManifestDeliveredToStore(manifestId);
    }

    @PostMapping("/locations")
    public TruckResponse updateTruckLocation(
            @RequestBody TruckLocationUpdateRequest request
    ) {
        return truckLogisticsService.updateTruckLocation(request);
    }
    @GetMapping("/stores/{storeId}/incoming-stops")
    public List<TruckStopResponse> getIncomingStopsForStore(
            @PathVariable UUID storeId
    ) {
        return truckLogisticsService.getIncomingStopsForStore(storeId);
    }
    @GetMapping("/trucks/{truckId}/live-location")
    public TruckLiveLocationResponse getLiveLocation(
            @PathVariable UUID truckId
    ) {
        return truckLogisticsService.getLiveLocation(truckId);
    }

    @GetMapping("/trucks/live")
    public List<TruckLiveLocationResponse> getAllLiveTrucks() {
        return truckLogisticsService.getAllLiveTrucks();
    }
    @PatchMapping("/manifests/{manifestId}/deliver-store/{storeId}")
    public ManifestResponse deliverManifestItemsToStore(
            @PathVariable UUID manifestId,
            @PathVariable UUID storeId
    ) {
        return truckLogisticsService.deliverManifestItemsToStore(
                manifestId,
                storeId
        );
    }
    @GetMapping("/manifests/{manifestId}/items")
    public List<ManifestItemResponse> getManifestItems(
            @PathVariable UUID manifestId
    ) {
        return truckLogisticsService.getManifestItems(manifestId);
    }
    @GetMapping("/manifests/{manifestId}/stores/{storeId}/items")
    public List<ManifestItemResponse> getManifestItemsByStore(
            @PathVariable UUID manifestId,
            @PathVariable UUID storeId
    ) {
        return truckLogisticsService.getManifestItemsByStore(
                manifestId,
                storeId
        );
    }
}