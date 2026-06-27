package com.example.whitefox.trucklogistics.service;

import com.example.whitefox.store.entity.Store;
import com.example.whitefox.store.repository.StoreRepository;
import com.example.whitefox.tracking.entity.Garment;
import com.example.whitefox.tracking.enums.GarmentStatus;
import com.example.whitefox.tracking.repository.GarmentRepository;
import com.example.whitefox.trucklogistics.dto.*;
import com.example.whitefox.trucklogistics.entity.*;
import com.example.whitefox.trucklogistics.enums.TruckManifestStatus;
import com.example.whitefox.trucklogistics.enums.TruckStatus;
import com.example.whitefox.trucklogistics.enums.TruckTripStatus;
import com.example.whitefox.trucklogistics.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TruckLogisticsServiceImpl implements TruckLogisticsService {

    private final TruckRepository truckRepository;
    private final TruckTripRepository tripRepository;
    private final TruckTripStopRepository stopRepository;
    private final TruckManifestRepository manifestRepository;
    private final TruckManifestItemRepository manifestItemRepository;
    private final TruckLocationLogRepository locationLogRepository;
    private final StoreRepository storeRepository;
    private final GarmentRepository garmentRepository;

    @Override
    public TruckResponse createTruck(CreateTruckRequest request) {
        Truck truck = Truck.builder()
                .truckNumber(request.getTruckNumber())
                .driverName(request.getDriverName())
                .driverPhone(request.getDriverPhone())
                .capacityKg(request.getCapacityKg())
                .status(TruckStatus.AVAILABLE)
                .active(true)
                .build();

        return mapTruck(truckRepository.save(truck));
    }

    @Override
    public List<TruckResponse> getAllTrucks() {
        return truckRepository.findAll().stream().map(this::mapTruck).toList();
    }

    @Override
    public TruckResponse getTruck(UUID truckId) {
        return mapTruck(getTruckEntity(truckId));
    }

    @Override
    public TruckTripResponse createTrip(CreateTruckTripRequest request) {
        Truck truck = getTruckEntity(request.getTruckId());

        TruckTrip trip = TruckTrip.builder()
                .truck(truck)
                .tripNumber("TRIP-" + System.currentTimeMillis())
                .shift(request.getShift())
                .returnTrip(request.getReturnTrip())
                .status(TruckTripStatus.TRIP_CREATED)
                .totalStores(0)
                .completedStores(0)
                .totalGarments(0)
                .totalWeight(0.0)
                .build();

        return mapTrip(tripRepository.save(trip));
    }

    @Override
    public TruckTripResponse startTrip(UUID tripId) {
        TruckTrip trip = getTripEntity(tripId);
        trip.setStatus(TruckTripStatus.TRIP_STARTED);
        trip.setStartTime(LocalDateTime.now());

        Truck truck = trip.getTruck();
        truck.setStatus(TruckStatus.ON_TRIP);
        truckRepository.save(truck);

        return mapTrip(tripRepository.save(trip));
    }

    @Override
    public TruckTripResponse completeTrip(UUID tripId) {
        TruckTrip trip = getTripEntity(tripId);
        trip.setStatus(TruckTripStatus.TRIP_COMPLETED);
        trip.setEndTime(LocalDateTime.now());

        Truck truck = trip.getTruck();
        truck.setStatus(TruckStatus.AVAILABLE);
        truckRepository.save(truck);

        return mapTrip(tripRepository.save(trip));
    }

    @Override
    public List<TruckTripResponse> getTripsByTruck(UUID truckId) {
        return tripRepository.findByTruckId(truckId).stream().map(this::mapTrip).toList();
    }

    @Override
    public TruckStopResponse addStop(CreateTruckStopRequest request) {
        TruckTrip trip = getTripEntity(request.getTripId());

        Store store = null;
        if (request.getStoreId() != null) {
            store = storeRepository.findById(request.getStoreId())
                    .orElseThrow(() -> new RuntimeException("Store not found"));
        }

        TruckTripStop stop = TruckTripStop.builder()
                .trip(trip)
                .store(store)
                .stopType(request.getStopType())
                .stopSequence(request.getStopSequence())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .expectedArrivalTime(request.getExpectedArrivalTime())
                .status("PENDING")
                .build();

        TruckTripStop saved = stopRepository.save(stop);

        trip.setTotalStores(stopRepository.findByTripIdOrderByStopSequenceAsc(trip.getId()).size());
        tripRepository.save(trip);

        return mapStop(saved);
    }

    @Override
    public List<TruckStopResponse> getStopsByTrip(UUID tripId) {
        return stopRepository.findByTripIdOrderByStopSequenceAsc(tripId)
                .stream().map(this::mapStop).toList();
    }

    @Override
    public List<TruckStopResponse> getIncomingStopsForStore(UUID storeId) {
        return stopRepository.findByStoreId(storeId)
                .stream()
                .filter(stop -> !"COMPLETED".equals(stop.getStatus()))
                .map(this::mapStop)
                .toList();
    }

    @Override
    public TruckStopResponse markStopReached(UUID stopId) {
        TruckTripStop stop = getStopEntity(stopId);
        stop.setStatus("REACHED");
        stop.setActualArrivalTime(LocalDateTime.now());

        stop.getTrip().setStatus(TruckTripStatus.STORE_REACHED);
        tripRepository.save(stop.getTrip());

        return mapStop(stopRepository.save(stop));
    }

    @Override
    public TruckStopResponse markStopCompleted(UUID stopId) {
        TruckTripStop stop = getStopEntity(stopId);
        stop.setStatus("COMPLETED");
        stop.setDepartureTime(LocalDateTime.now());

        TruckTrip trip = stop.getTrip();
        trip.setCompletedStores(trip.getCompletedStores() == null ? 1 : trip.getCompletedStores() + 1);
        tripRepository.save(trip);

        return mapStop(stopRepository.save(stop));
    }

    @Override
    public TruckResponse updateTruckLocation(TruckLocationUpdateRequest request) {
        Truck truck = getTruckEntity(request.getTruckId());

        TruckTrip trip = null;
        if (request.getTripId() != null) {
            trip = getTripEntity(request.getTripId());
        }

        truck.setCurrentLatitude(request.getLatitude());
        truck.setCurrentLongitude(request.getLongitude());

        Truck savedTruck = truckRepository.save(truck);

        TruckLocationLog log = TruckLocationLog.builder()
                .truck(savedTruck)
                .trip(trip)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .speed(request.getSpeed())
                .heading(request.getHeading())
                .build();

        locationLogRepository.save(log);

        return mapTruck(savedTruck);
    }

    @Override
    public TruckLiveLocationResponse getLiveLocation(UUID truckId) {
        Truck truck = getTruckEntity(truckId);

        TruckTrip activeTrip = tripRepository.findByTruckId(truckId)
                .stream()
                .filter(t -> t.getStatus() != TruckTripStatus.TRIP_COMPLETED)
                .filter(t -> t.getStatus() != TruckTripStatus.CANCELLED)
                .findFirst()
                .orElse(null);

        return mapLiveLocation(truck, activeTrip);
    }

    @Override
    public List<TruckLiveLocationResponse> getAllLiveTrucks() {
        return truckRepository.findAll()
                .stream()
                .filter(t -> t.getCurrentLatitude() != null)
                .filter(t -> t.getCurrentLongitude() != null)
                .map(truck -> {
                    TruckTrip activeTrip = tripRepository.findByTruckId(truck.getId())
                            .stream()
                            .filter(trip -> trip.getStatus() != TruckTripStatus.TRIP_COMPLETED)
                            .filter(trip -> trip.getStatus() != TruckTripStatus.CANCELLED)
                            .findFirst()
                            .orElse(null);

                    return mapLiveLocation(truck, activeTrip);
                })
                .toList();
    }

    @Override
    public ManifestResponse createManifest(CreateManifestRequest request) {
        TruckTrip trip = getTripEntity(request.getTripId());

        Store sourceStore = null;
        Store destinationStore = null;

        if (request.getSourceStoreId() != null) {
            sourceStore = storeRepository.findById(request.getSourceStoreId())
                    .orElseThrow(() -> new RuntimeException("Source store not found"));
        }

        if (request.getDestinationStoreId() != null) {
            destinationStore = storeRepository.findById(request.getDestinationStoreId())
                    .orElseThrow(() -> new RuntimeException("Destination store not found"));
        }

        TruckManifest manifest = TruckManifest.builder()
                .trip(trip)
                .sourceStore(sourceStore)
                .destinationStore(destinationStore)
                .movementType(request.getMovementType())
                .manifestNumber("MAN-" + System.currentTimeMillis())
                .totalGarments(0)
                .totalWeight(0.0)
                .status(TruckManifestStatus.CREATED)
                .build();

        return mapManifest(manifestRepository.save(manifest));
    }

    @Override
    public ManifestResponse addManifestItem(UUID manifestId, AddManifestItemRequest request) {
        TruckManifest manifest = getManifestEntity(manifestId);

        Garment garment = null;
        if (request.getGarmentId() != null) {
            garment = garmentRepository.findById(request.getGarmentId())
                    .orElseThrow(() -> new RuntimeException("Garment not found"));
        }

        Store destinationStore = null;
        if (request.getDestinationStoreId() != null) {
            destinationStore = storeRepository.findById(request.getDestinationStoreId())
                    .orElseThrow(() -> new RuntimeException("Destination store not found"));
        }

        TruckManifestItem item = TruckManifestItem.builder()
                .manifest(manifest)
                .garment(garment)
                .destinationStore(destinationStore)
                .qrCode(request.getQrCode())
                .status("LOADED")
                .loadedAt(LocalDateTime.now())
                .build();

        manifestItemRepository.save(item);

        if (garment != null) {
            if ("STORE_TO_HQ".equalsIgnoreCase(manifest.getMovementType())) {
                garment.setStatus(GarmentStatus.LOADED_ON_TRUCK);
            }

            if ("HQ_TO_STORE".equalsIgnoreCase(manifest.getMovementType())) {
                garment.setStatus(GarmentStatus.LOADED_FOR_STORE);
            }

            garmentRepository.save(garment);
        }

        manifest.setTotalGarments(manifestItemRepository.findByManifestId(manifestId).size());
        manifestRepository.save(manifest);

        return mapManifest(manifest);
    }

    @Override
    public List<ManifestResponse> getManifestsByTrip(UUID tripId) {
        return manifestRepository.findByTripId(tripId).stream().map(this::mapManifest).toList();
    }

    @Override
    public ManifestResponse markManifestDeliveredToHq(UUID manifestId) {
        TruckManifest manifest = getManifestEntity(manifestId);

        manifest.setStatus(TruckManifestStatus.DELIVERED_TO_HQ);
        manifest.setDeliveredAt(LocalDateTime.now());
        manifestRepository.save(manifest);

        manifestItemRepository.findByManifestId(manifestId).forEach(item -> {
            item.setStatus("DELIVERED");
            item.setDeliveredAt(LocalDateTime.now());

            if (item.getGarment() != null) {
                item.getGarment().setStatus(GarmentStatus.RECEIVED_AT_HQ);
                garmentRepository.save(item.getGarment());
            }

            manifestItemRepository.save(item);
        });

        return mapManifest(manifest);
    }

    @Override
    public ManifestResponse markManifestDeliveredToStore(UUID manifestId) {
        TruckManifest manifest = getManifestEntity(manifestId);

        manifest.setStatus(TruckManifestStatus.DELIVERED_TO_STORE);
        manifest.setDeliveredAt(LocalDateTime.now());
        manifestRepository.save(manifest);

        manifestItemRepository.findByManifestId(manifestId).forEach(item -> {
            item.setStatus("DELIVERED");
            item.setDeliveredAt(LocalDateTime.now());

            if (item.getGarment() != null) {
                item.getGarment().setStatus(GarmentStatus.RECEIVED_AT_STORE_AFTER_PROCESSING);
                garmentRepository.save(item.getGarment());
            }

            manifestItemRepository.save(item);
        });

        return mapManifest(manifest);
    }

    @Override
    public ManifestResponse deliverManifestItemsToStore(UUID manifestId, UUID storeId) {
        TruckManifest manifest = getManifestEntity(manifestId);

        List<TruckManifestItem> items = manifestItemRepository.findByManifestId(manifestId);

        items.stream()
                .filter(item -> item.getDestinationStore() != null)
                .filter(item -> item.getDestinationStore().getId().equals(storeId))
                .forEach(item -> {
                    item.setStatus("DELIVERED");
                    item.setDeliveredAt(LocalDateTime.now());

                    if (item.getGarment() != null) {
                        item.getGarment().setStatus(GarmentStatus.RECEIVED_AT_STORE_AFTER_PROCESSING);
                        garmentRepository.save(item.getGarment());
                    }

                    manifestItemRepository.save(item);
                });

        boolean allDelivered = items.stream()
                .allMatch(item -> "DELIVERED".equals(item.getStatus()));

        if (allDelivered) {
            manifest.setStatus(TruckManifestStatus.DELIVERED_TO_STORE);
            manifest.setDeliveredAt(LocalDateTime.now());
            manifestRepository.save(manifest);
        }

        return mapManifest(manifest);
    }

    private Truck getTruckEntity(UUID truckId) {
        return truckRepository.findById(truckId)
                .orElseThrow(() -> new RuntimeException("Truck not found"));
    }

    private TruckTrip getTripEntity(UUID tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Truck trip not found"));
    }

    private TruckTripStop getStopEntity(UUID stopId) {
        return stopRepository.findById(stopId)
                .orElseThrow(() -> new RuntimeException("Truck stop not found"));
    }

    private TruckManifest getManifestEntity(UUID manifestId) {
        return manifestRepository.findById(manifestId)
                .orElseThrow(() -> new RuntimeException("Truck manifest not found"));
    }

    private TruckResponse mapTruck(Truck truck) {
        return TruckResponse.builder()
                .id(truck.getId())
                .truckNumber(truck.getTruckNumber())
                .driverName(truck.getDriverName())
                .driverPhone(truck.getDriverPhone())
                .capacityKg(truck.getCapacityKg())
                .currentLatitude(truck.getCurrentLatitude())
                .currentLongitude(truck.getCurrentLongitude())
                .status(truck.getStatus())
                .active(truck.getActive())
                .build();
    }

    private TruckTripResponse mapTrip(TruckTrip trip) {
        return TruckTripResponse.builder()
                .id(trip.getId())
                .truckId(trip.getTruck().getId())
                .truckNumber(trip.getTruck().getTruckNumber())
                .tripNumber(trip.getTripNumber())
                .shift(trip.getShift())
                .startTime(trip.getStartTime())
                .endTime(trip.getEndTime())
                .status(trip.getStatus())
                .totalStores(trip.getTotalStores())
                .completedStores(trip.getCompletedStores())
                .totalGarments(trip.getTotalGarments())
                .totalWeight(trip.getTotalWeight())
                .returnTrip(trip.getReturnTrip())
                .build();
    }

    private TruckStopResponse mapStop(TruckTripStop stop) {
        return TruckStopResponse.builder()
                .id(stop.getId())
                .tripId(stop.getTrip().getId())
                .storeId(stop.getStore() != null ? stop.getStore().getId() : null)
                .storeName(stop.getStore() != null ? stop.getStore().getName() : null)
                .stopType(stop.getStopType())
                .stopSequence(stop.getStopSequence())
                .latitude(stop.getLatitude())
                .longitude(stop.getLongitude())
                .expectedArrivalTime(stop.getExpectedArrivalTime())
                .actualArrivalTime(stop.getActualArrivalTime())
                .departureTime(stop.getDepartureTime())
                .garmentsLoaded(stop.getGarmentsLoaded())
                .garmentsUnloaded(stop.getGarmentsUnloaded())
                .weightLoaded(stop.getWeightLoaded())
                .weightUnloaded(stop.getWeightUnloaded())
                .status(stop.getStatus())
                .build();
    }

    private ManifestResponse mapManifest(TruckManifest manifest) {
        return ManifestResponse.builder()
                .id(manifest.getId())
                .tripId(manifest.getTrip().getId())
                .tripNumber(manifest.getTrip().getTripNumber())
                .sourceStoreId(manifest.getSourceStore() != null ? manifest.getSourceStore().getId() : null)
                .sourceStoreName(manifest.getSourceStore() != null ? manifest.getSourceStore().getName() : null)
                .destinationStoreId(manifest.getDestinationStore() != null ? manifest.getDestinationStore().getId() : null)
                .destinationStoreName(manifest.getDestinationStore() != null ? manifest.getDestinationStore().getName() : null)
                .movementType(manifest.getMovementType())
                .manifestNumber(manifest.getManifestNumber())
                .totalGarments(manifest.getTotalGarments())
                .totalWeight(manifest.getTotalWeight())
                .status(manifest.getStatus())
                .loadedAt(manifest.getLoadedAt())
                .deliveredAt(manifest.getDeliveredAt())
                .build();
    }

    private TruckLiveLocationResponse mapLiveLocation(Truck truck, TruckTrip activeTrip) {
        return TruckLiveLocationResponse.builder()
                .truckId(truck.getId())
                .truckNumber(truck.getTruckNumber())
                .driverName(truck.getDriverName())
                .driverPhone(truck.getDriverPhone())
                .activeTripId(activeTrip != null ? activeTrip.getId() : null)
                .tripNumber(activeTrip != null ? activeTrip.getTripNumber() : null)
                .latitude(truck.getCurrentLatitude())
                .longitude(truck.getCurrentLongitude())
                .status(truck.getStatus() != null ? truck.getStatus().name() : null)
                .build();
    }
    @Override
    public List<ManifestItemResponse> getManifestItems(UUID manifestId) {
        return manifestItemRepository.findByManifestId(manifestId)
                .stream()
                .map(this::mapManifestItem)
                .toList();
    }

    private ManifestItemResponse mapManifestItem(TruckManifestItem item) {
        return ManifestItemResponse.builder()
                .id(item.getId())
                .manifestId(item.getManifest().getId())
                .garmentId(item.getGarment() != null ? item.getGarment().getId() : null)
                .itemName(item.getGarment() != null ? item.getGarment().getItemName() : null)
                .serviceType(item.getGarment() != null ? item.getGarment().getServiceType() : null)
                .qrCode(item.getQrCode())
                .destinationStoreId(item.getDestinationStore() != null ? item.getDestinationStore().getId() : null)
                .destinationStoreName(item.getDestinationStore() != null ? item.getDestinationStore().getName() : null)
                .status(item.getStatus())
                .loadedAt(item.getLoadedAt())
                .deliveredAt(item.getDeliveredAt())
                .build();
    }
    @Override
    public List<ManifestItemResponse> getManifestItemsByStore(
            UUID manifestId,
            UUID storeId
    ) {
        return manifestItemRepository.findByManifestId(manifestId)
                .stream()
                .filter(item -> item.getDestinationStore() != null)
                .filter(item -> item.getDestinationStore().getId().equals(storeId))
                .map(this::mapManifestItem)
                .toList();
    }
}