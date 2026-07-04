package com.example.whitefox.customerbooking.service;

import com.example.whitefox.customerbooking.dto.*;
import com.example.whitefox.customerbooking.entity.CustomerBooking;
import com.example.whitefox.customerbooking.entity.CustomerBookingItem;
import com.example.whitefox.customerbooking.enums.CustomerBookingStatus;
import com.example.whitefox.customerbooking.repository.CustomerBookingItemRepository;
import com.example.whitefox.customerbooking.repository.CustomerBookingRepository;
import com.example.whitefox.customers.entity.Customer;
import com.example.whitefox.customers.repository.CustomerRepository;
import com.example.whitefox.customerupdates.dto.CreateCustomerUpdateRequest;
import com.example.whitefox.customerupdates.enums.CustomerUpdateType;
import com.example.whitefox.customerupdates.service.CustomerUpdateService;
import com.example.whitefox.garments.entity.ServiceCatalog;
import com.example.whitefox.garments.repository.ServiceCatalogRepository;
import com.example.whitefox.riders.entity.Rider;
import com.example.whitefox.riders.repository.RiderRepository;
import com.example.whitefox.store.entity.Store;
import com.example.whitefox.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.whitefox.ordertimeline.service.OrderTimelineService;
import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.enums.OrderStatus;
import com.example.whitefox.realtime.dto.RealtimeEvent;
import com.example.whitefox.realtime.service.RealtimeEventService;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerBookingServiceImpl implements CustomerBookingService {
//    private final RealtimeEventService realtimeEventService;
    private final CustomerBookingRepository bookingRepository;
    private final CustomerBookingItemRepository itemRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final RiderRepository riderRepository;
    private final ServiceCatalogRepository catalogRepository;
    private final CustomerUpdateService customerUpdateService;
    private final OrderTimelineService orderTimelineService;
    private final RealtimeEventService realtimeEventService;
    @Override
    public CustomerBookingResponse createBooking(CreateCustomerBookingRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Store store = null;

        if (request.getStoreId() != null) {
            store = storeRepository.findById(request.getStoreId())
                    .orElseThrow(() -> new RuntimeException("Store not found"));
        }

        CustomerBooking booking = CustomerBooking.builder()
                .customer(customer)
                .store(store)
                .pickupAddress(request.getPickupAddress())
                .pickupDate(request.getPickupDate())
                .pickupTimeSlot(request.getPickupTimeSlot())
                .specialInstructions(request.getSpecialInstructions())
                .status(CustomerBookingStatus.REQUESTED)
                .build();

        CustomerBooking savedBooking = bookingRepository.save(booking);

        double estimatedAmount = 0.0;

        for (CreateCustomerBookingItemRequest itemRequest : request.getItems()) {

            ServiceCatalog catalog = catalogRepository.findById(itemRequest.getCatalogId())
                    .orElseThrow(() -> new RuntimeException("Catalog item not found"));

            double itemTotal = catalog.getPrice() * itemRequest.getQuantity();

            CustomerBookingItem item = CustomerBookingItem.builder()
                    .booking(savedBooking)
                    .serviceCatalog(catalog)
                    .serviceType(catalog.getServiceType())
                    .itemName(catalog.getItemName())
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(catalog.getPrice())
                    .estimatedPrice(itemTotal)
                    .build();

            itemRepository.save(item);

            estimatedAmount += itemTotal;
        }

        savedBooking.setEstimatedAmount(estimatedAmount);
        CustomerBooking finalBooking = bookingRepository.save(savedBooking);

        createUpdate(
                finalBooking,
                CustomerUpdateType.BOOKING_CREATED,
                "Booking Created",
                "Your pickup booking has been created successfully."
        );

        return map(finalBooking);
    }

    @Override
    public List<CustomerBookingResponse> getBookingsByCustomer(UUID customerId) {
        return bookingRepository.findByCustomerId(customerId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public CustomerBookingResponse rescheduleBooking(
            UUID bookingId,
            RescheduleBookingRequest request
    ) {
        CustomerBooking booking = getBookingEntity(bookingId);

        if (booking.getStatus() == CustomerBookingStatus.CANCELLED ||
                booking.getStatus() == CustomerBookingStatus.PICKUP_BILL_CREATED) {
            throw new RuntimeException(
                    "Booking cannot be rescheduled at this stage"
            );
        }

        booking.setPickupDate(request.getPickupDate());
        booking.setPickupTimeSlot(request.getPickupTimeSlot());

        CustomerBooking saved = bookingRepository.save(booking);

        createUpdate(
                saved,
                CustomerUpdateType.BOOKING_CREATED,
                "Pickup Rescheduled",
                "Your pickup has been rescheduled to "
                        + request.getPickupDate()
                        + " at "
                        + request.getPickupTimeSlot()
        );

        return map(saved);
    }

    @Override
    public List<CustomerBookingResponse> getBookingsByStore(UUID storeId) {
        return bookingRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public CustomerBookingResponse getBooking(UUID bookingId) {
        return map(getBookingEntity(bookingId));
    }
    @Override
    public CustomerBookingResponse assignStore(UUID bookingId, AssignStoreRequest request) {

        CustomerBooking booking = getBookingEntity(bookingId);

        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found"));

        booking.setStore(store);
        booking.setStatus(CustomerBookingStatus.STORE_ASSIGNED);

        CustomerBooking saved = bookingRepository.save(booking);

        createUpdate(
                saved,
                CustomerUpdateType.STORE_ASSIGNED,
                "Store Assigned",
                store.getName() + " has been assigned for your pickup."
        );

        return map(saved);
    }

    @Override
    public CustomerBookingResponse assignRider(UUID bookingId, AssignRiderRequest request) {

        CustomerBooking booking = getBookingEntity(bookingId);

        Rider rider = riderRepository.findById(request.getRiderId())
                .orElseThrow(() -> new RuntimeException("Rider not found"));

        booking.setAssignedRider(rider);
        booking.setStatus(CustomerBookingStatus.RIDER_ASSIGNED);

        CustomerBooking saved = bookingRepository.save(booking);

        createUpdate(
                saved,
                CustomerUpdateType.RIDER_ASSIGNED,
                "Rider Assigned",
                rider.getName() + " has been assigned for pickup."
        );

        realtimeEventService.sendToRider(
                rider.getId(),
                RealtimeEvent.builder()
                        .type("PICKUP_ASSIGNED")
                        .title("New Pickup Assigned")
                        .message("A new pickup job has been assigned to you.")
                        .referenceId(saved.getId())
                        .status(saved.getStatus().name())
                        .build()
        );

        realtimeEventService.sendToCustomer(
                saved.getCustomer().getId(),
                RealtimeEvent.builder()
                        .type("RIDER_ASSIGNED")
                        .title("Rider Assigned")
                        .message(rider.getName() + " has been assigned for your pickup.")
                        .referenceId(saved.getId())
                        .status(saved.getStatus().name())
                        .build()
        );

        return map(saved);
    }
    @Override
    public CustomerBookingResponse markRiderOnTheWay(UUID bookingId) {

        CustomerBooking booking = getBookingEntity(bookingId);
        booking.setStatus(CustomerBookingStatus.RIDER_ON_THE_WAY);

        CustomerBooking saved = bookingRepository.save(booking);

        createUpdate(
                saved,
                CustomerUpdateType.RIDER_ON_THE_WAY,
                "Rider On The Way",
                "Your rider is on the way for pickup."
        );

        realtimeEventService.sendToCustomer(
                saved.getCustomer().getId(),
                RealtimeEvent.builder()
                        .type("RIDER_ON_THE_WAY")
                        .title("Rider On The Way")
                        .message("Your rider is on the way for pickup.")
                        .referenceId(saved.getId())
                        .status(saved.getStatus().name())
                        .build()
        );

        return map(saved);
    }
    @Override
    public CustomerBookingResponse markRiderReached(UUID bookingId) {

        CustomerBooking booking = getBookingEntity(bookingId);
        booking.setStatus(CustomerBookingStatus.RIDER_REACHED);

        CustomerBooking saved = bookingRepository.save(booking);

        createUpdate(
                saved,
                CustomerUpdateType.RIDER_REACHED,
                "Rider Reached",
                "Your rider has reached the pickup location."
        );

        // Notify Customer instantly
        realtimeEventService.sendToCustomer(
                saved.getCustomer().getId(),
                RealtimeEvent.builder()
                        .type("RIDER_REACHED")
                        .title("Rider Reached")
                        .message("Your rider has arrived at your pickup location.")
                        .referenceId(saved.getId())
                        .status(saved.getStatus().name())
                        .build()
        );

        // Notify Rider instantly
        realtimeEventService.sendToRider(
                saved.getAssignedRider().getId(),
                RealtimeEvent.builder()
                        .type("ARRIVED_AT_CUSTOMER")
                        .title("Reached Customer")
                        .message("You have reached the customer's pickup location.")
                        .referenceId(saved.getId())
                        .status(saved.getStatus().name())
                        .build()
        );

        // Notify Store instantly
        if (saved.getStore() != null) {
            realtimeEventService.sendToStore(
                    saved.getStore().getId(),
                    RealtimeEvent.builder()
                            .type("RIDER_REACHED_CUSTOMER")
                            .title("Rider Reached Customer")
                            .message("Pickup rider has reached the customer's location.")
                            .referenceId(saved.getId())
                            .status(saved.getStatus().name())
                            .build()
            );
        }

        return map(saved);
    }

    @Override
    public CustomerBookingResponse markPickupBillCreated(UUID bookingId) {

        CustomerBooking booking = getBookingEntity(bookingId);
        booking.setStatus(CustomerBookingStatus.PICKUP_BILL_CREATED);

        CustomerBooking saved = bookingRepository.save(booking);

        createUpdate(
                saved,
                CustomerUpdateType.PICKUP_BILL_CREATED,
                "Pickup Bill Created",
                "Your pickup bill has been created and sent for store verification."
        );

        realtimeEventService.sendToCustomer(
                saved.getCustomer().getId(),
                RealtimeEvent.builder()
                        .type("PICKUP_BILL_CREATED")
                        .title("Pickup Bill Created")
                        .message("Your pickup bill has been created.")
                        .referenceId(saved.getId())
                        .status(saved.getStatus().name())
                        .build()
        );

        if (saved.getStore() != null) {
            realtimeEventService.sendToStore(
                    saved.getStore().getId(),
                    RealtimeEvent.builder()
                            .type("PICKUP_BILL_FOR_VERIFICATION")
                            .title("Pickup Bill Verification")
                            .message("A pickup bill is waiting for store verification.")
                            .referenceId(saved.getId())
                            .status(saved.getStatus().name())
                            .build()
            );
        }

        realtimeEventService.sendToAdmin(
                RealtimeEvent.builder()
                        .type("PICKUP_BILL_CREATED")
                        .title("Pickup Bill Created")
                        .message("A new pickup bill has been created.")
                        .referenceId(saved.getId())
                        .status(saved.getStatus().name())
                        .build()
        );

        return map(saved);
    }
    @Override
    public CustomerBookingResponse cancelBooking(UUID bookingId) {

        CustomerBooking booking = getBookingEntity(bookingId);
        booking.setStatus(CustomerBookingStatus.CANCELLED);

        CustomerBooking saved = bookingRepository.save(booking);

        createUpdate(
                saved,
                CustomerUpdateType.CANCELLED,
                "Booking Cancelled",
                "Your pickup booking has been cancelled."
        );

        return map(saved);
    }

    private CustomerBooking getBookingEntity(UUID bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Customer booking not found"));
    }

    private void createUpdate(
            CustomerBooking booking,
            CustomerUpdateType type,
            String title,
            String description
    ) {
        CreateCustomerUpdateRequest request = new CreateCustomerUpdateRequest();

        request.setCustomerId(booking.getCustomer().getId());
        request.setOrderId(null);
        request.setUpdateType(type);
        request.setTitle(title);
        request.setDescription(description);

        customerUpdateService.createUpdate(request);
    }

    private CustomerBookingResponse map(CustomerBooking booking) {

        List<CustomerBookingItemResponse> items =
                itemRepository.findByBookingId(booking.getId())
                        .stream()
                        .map(item -> CustomerBookingItemResponse.builder()
                                .catalogId(
                                        item.getServiceCatalog() != null
                                                ? item.getServiceCatalog().getId()
                                                : null
                                )
                                .serviceType(item.getServiceType())
                                .itemName(item.getItemName())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .estimatedPrice(item.getEstimatedPrice())
                                .build())
                        .toList();

        return CustomerBookingResponse.builder()
                .id(booking.getId())
                .customerId(booking.getCustomer().getId())
                .customerName(booking.getCustomer().getName())

                .storeId(booking.getStore() != null ? booking.getStore().getId() : null)
                .storeName(booking.getStore() != null ? booking.getStore().getName() : null)

                .assignedRiderId(
                        booking.getAssignedRider() != null
                                ? booking.getAssignedRider().getId()
                                : null
                )
                .assignedRiderName(
                        booking.getAssignedRider() != null
                                ? booking.getAssignedRider().getName()
                                : null
                )

                .pickupAddress(booking.getPickupAddress())
                .pickupDate(booking.getPickupDate())
                .pickupTimeSlot(booking.getPickupTimeSlot())
                .specialInstructions(booking.getSpecialInstructions())
                .estimatedAmount(booking.getEstimatedAmount())
                .status(booking.getStatus())
                .items(items)
                .build();
    }
    @Override
    public List<CustomerBookingResponse> getBookingsByRider(UUID riderId) {
        return bookingRepository.findByAssignedRiderId(riderId)
                .stream()
                .map(this::map)
                .toList();
    }
}