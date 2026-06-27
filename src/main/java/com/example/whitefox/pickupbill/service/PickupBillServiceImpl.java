package com.example.whitefox.pickupbill.service;

import com.example.whitefox.customerbooking.entity.CustomerBooking;
import com.example.whitefox.customerbooking.enums.CustomerBookingStatus;
import com.example.whitefox.customerbooking.repository.CustomerBookingRepository;
import com.example.whitefox.customers.entity.Customer;
import com.example.whitefox.customers.repository.CustomerRepository;
import com.example.whitefox.orders.dto.OrderResponse;
import com.example.whitefox.orders.service.OrderService;
import com.example.whitefox.pickupbill.dto.*;
import com.example.whitefox.pickupbill.entity.PickupBill;
import com.example.whitefox.pickupbill.entity.PickupBillItem;
import com.example.whitefox.pickupbill.enums.PickupBillStatus;
import com.example.whitefox.pickupbill.repository.PickupBillItemRepository;
import com.example.whitefox.pickupbill.repository.PickupBillRepository;
import com.example.whitefox.riders.entity.Rider;
import com.example.whitefox.riders.repository.RiderRepository;
import com.example.whitefox.store.entity.Store;
import com.example.whitefox.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import com.example.whitefox.realtime.dto.RealtimeEvent;
import com.example.whitefox.realtime.service.RealtimeEventService;
@Service
@RequiredArgsConstructor
public class PickupBillServiceImpl implements PickupBillService {

    private final PickupBillRepository pickupBillRepository;
    private final PickupBillItemRepository pickupBillItemRepository;
    private final CustomerRepository customerRepository;
    private final RiderRepository riderRepository;
    private final StoreRepository storeRepository;
    private final OrderService orderService;
    private final CustomerBookingRepository bookingRepository;
    private final RealtimeEventService realtimeEventService;
    @Override
    public PickupBillResponse createPickupBill(CreatePickupBillRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Rider rider = riderRepository.findById(request.getRiderId())
                .orElseThrow(() -> new RuntimeException("Rider not found"));

        Store assignedStore = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found"));

        CustomerBooking booking = null;

        if (request.getCustomerBookingId() != null) {
            booking = bookingRepository.findById(request.getCustomerBookingId())
                    .orElseThrow(() -> new RuntimeException("Customer booking not found"));

            booking.setStatus(CustomerBookingStatus.PICKUP_BILL_CREATED);
            bookingRepository.save(booking);
        }

        double subtotal = request.getItems()
                .stream()
                .mapToDouble(i -> i.getQuantity() * i.getUnitPrice())
                .sum();

        double gst = subtotal * 0.18;
        double totalAmount = subtotal + gst;

        PickupBill bill = PickupBill.builder()
                .customerBooking(booking)
                .billNumber("PB-" + System.currentTimeMillis())
                .customer(customer)
                .rider(rider)
                .store(assignedStore)
                .assignedStore(assignedStore)
                .pickupAddress(request.getPickupAddress())
                .pickupLatitude(request.getPickupLatitude())
                .pickupLongitude(request.getPickupLongitude())
                .subtotal(subtotal)
                .gst(gst)
                .totalAmount(totalAmount)
                .status(PickupBillStatus.CREATED_BY_RIDER)
                .build();

        PickupBill savedBill = pickupBillRepository.save(bill);

        List<PickupBillItem> items = request.getItems()
                .stream()
                .map(i -> PickupBillItem.builder()
                        .pickupBill(savedBill)
                        .itemName(i.getItemName())
                        .serviceType(i.getServiceType())
                        .quantity(i.getQuantity())
                        .unitPrice(i.getUnitPrice())
                        .totalPrice(i.getQuantity() * i.getUnitPrice())
                        .conditionNote(i.getConditionNote())
                        .photoUrl(i.getPhotoUrl())
                        .build())
                .toList();

        pickupBillItemRepository.saveAll(items);

        return map(savedBill);
    }

    @Override
    public List<PickupBillResponse> getAllPickupBills() {
        return pickupBillRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public PickupBillResponse getPickupBillById(UUID id) {
        PickupBill bill = pickupBillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pickup bill not found"));

        return map(bill);
    }

    @Override
    public PickupBillResponse verifyPickupBill(UUID id) {
        PickupBill bill = pickupBillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pickup bill not found"));

        bill.setStatus(PickupBillStatus.VERIFIED_BY_STORE);
        bill.setVerifiedAt(LocalDateTime.now());

        if (bill.getStore() != null) {
            bill.setVerificationStore(bill.getStore());
        }

        PickupBill saved = pickupBillRepository.save(bill);

        realtimeEventService.sendToCustomer(
                saved.getCustomer().getId(),
                RealtimeEvent.builder()
                        .type("PICKUP_BILL_VERIFIED")
                        .title("Pickup Bill Verified")
                        .message("Your pickup bill has been verified.")
                        .referenceId(saved.getId())
                        .status(saved.getStatus().name())
                        .build()
        );

        realtimeEventService.sendToRider(
                saved.getRider().getId(),
                RealtimeEvent.builder()
                        .type("STORE_VERIFIED_BILL")
                        .title("Store Verified Bill")
                        .message("Proceed to collect clothes.")
                        .referenceId(saved.getId())
                        .status(saved.getStatus().name())
                        .build()
        );

        if (saved.getStore() != null) {
            realtimeEventService.sendToStore(
                    saved.getStore().getId(),
                    RealtimeEvent.builder()
                            .type("BILL_VERIFIED")
                            .title("Bill Verified")
                            .message("Pickup bill verified successfully.")
                            .referenceId(saved.getId())
                            .status(saved.getStatus().name())
                            .build()
            );
        }

        realtimeEventService.sendToAdmin(
                RealtimeEvent.builder()
                        .type("BILL_VERIFIED")
                        .title("Pickup Bill Verified")
                        .message("Store has verified a pickup bill.")
                        .referenceId(saved.getId())
                        .status(saved.getStatus().name())
                        .build()
        );

        return map(saved);
    }

    @Override
    public OrderResponse convertToOrder(UUID id) {
        PickupBill bill = pickupBillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pickup bill not found"));

        if (bill.getStatus() == PickupBillStatus.CONVERTED_TO_ORDER) {
            throw new RuntimeException("This pickup bill is already converted to order");
        }
        System.out.println("CONVERT API HIT BILL ID = " + id);

        bill.setStatus(PickupBillStatus.CONVERTED_TO_ORDER);

        PickupBill saved = pickupBillRepository.save(bill);
        System.out.println("CONVERT TO ORDER CALLED FOR BILL = " + saved.getBillNumber());
        OrderResponse response =
                orderService.createOrderFromPickupBill(saved);
        System.out.println("ORDER CREATED ID = " + response.getId());
        System.out.println("BILL NUMBER = " + saved.getBillNumber());
        // keep your realtime events below this line
        realtimeEventService.sendToCustomer(
                saved.getCustomer().getId(),
                RealtimeEvent.builder()
                        .type("ORDER_CREATED")
                        .title("Laundry Order Created")
                        .message("Your pickup has been converted into a laundry order.")
                        .referenceId(saved.getId())
                        .status(saved.getStatus().name())
                        .build()
        );

        realtimeEventService.sendToRider(
                saved.getRider().getId(),
                RealtimeEvent.builder()
                        .type("ORDER_CREATED")
                        .title("Order Created")
                        .message("Pickup completed successfully.")
                        .referenceId(saved.getId())
                        .status(saved.getStatus().name())
                        .build()
        );

        if (saved.getStore() != null) {
            realtimeEventService.sendToStore(
                    saved.getStore().getId(),
                    RealtimeEvent.builder()
                            .type("ORDER_CREATED")
                            .title("Laundry Order Created")
                            .message("Pickup bill converted into order.")
                            .referenceId(saved.getId())
                            .status(saved.getStatus().name())
                            .build()
            );
        }

        realtimeEventService.sendToAdmin(
                RealtimeEvent.builder()
                        .type("ORDER_CREATED")
                        .title("Laundry Order Created")
                        .message("A new laundry order has been created.")
                        .referenceId(saved.getId())
                        .status(saved.getStatus().name())
                        .build()
        );

        return response;
    }
    @Override
    public PickupBillResponse submitForVerification(UUID pickupBillId) {
        PickupBill bill = pickupBillRepository.findById(pickupBillId)
                .orElseThrow(() -> new RuntimeException("Pickup bill not found"));

        bill.setStatus(PickupBillStatus.SENT_TO_STORE);

        return map(pickupBillRepository.save(bill));
    }

    @Override
    public PickupBillResponse collectClothes(UUID pickupBillId) {
        PickupBill bill = pickupBillRepository.findById(pickupBillId)
                .orElseThrow(() -> new RuntimeException("Pickup bill not found"));

        if (bill.getStatus() != PickupBillStatus.VERIFIED_BY_STORE) {
            throw new RuntimeException("Store verification required before collecting clothes");
        }

        bill.setStatus(PickupBillStatus.CLOTHES_COLLECTED);

        return map(pickupBillRepository.save(bill));
    }

    @Override
    public PickupBillResponse dropAtStore(
            UUID pickupBillId,
            UUID dropStoreId,
            Double dropLatitude,
            Double dropLongitude
    ) {
        PickupBill bill = pickupBillRepository.findById(pickupBillId)
                .orElseThrow(() -> new RuntimeException("Pickup bill not found"));

        Store dropStore = storeRepository.findById(dropStoreId)
                .orElseThrow(() -> new RuntimeException("Drop store not found"));

        if (bill.getStatus() != PickupBillStatus.CLOTHES_COLLECTED) {
            throw new RuntimeException("Clothes must be collected before dropping at store");
        }

        bill.setDropStore(dropStore);
        bill.setDropLatitude(dropLatitude);
        bill.setDropLongitude(dropLongitude);
        bill.setDroppedAt(LocalDateTime.now());
        bill.setStatus(PickupBillStatus.DROPPED_AT_STORE);

        return map(pickupBillRepository.save(bill));
    }

    @Override
    public PickupBillResponse receiveByStore(UUID pickupBillId) {
        PickupBill bill = pickupBillRepository.findById(pickupBillId)
                .orElseThrow(() -> new RuntimeException("Pickup bill not found"));

        if (bill.getStatus() != PickupBillStatus.DROPPED_AT_STORE) {
            throw new RuntimeException("Pickup bill must be dropped at store first");
        }

        bill.setReceivedAt(LocalDateTime.now());
        bill.setStatus(PickupBillStatus.RECEIVED_BY_STORE);

        return map(pickupBillRepository.save(bill));
    }

    private PickupBillResponse map(PickupBill bill) {

        List<PickupBillItemResponse> items =
                pickupBillItemRepository.findByPickupBillId(bill.getId())
                        .stream()
                        .map(item -> PickupBillItemResponse.builder()
                                .itemName(item.getItemName())
                                .serviceType(item.getServiceType())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .totalPrice(item.getTotalPrice())
                                .conditionNote(item.getConditionNote())
                                .photoUrl(item.getPhotoUrl())
                                .build())
                        .toList();

        return PickupBillResponse.builder()
                .id(bill.getId())
                .billNumber(bill.getBillNumber())

                .customerId(bill.getCustomer().getId())
                .customerName(bill.getCustomer().getName())

                .riderId(bill.getRider().getId())
                .riderName(bill.getRider().getName())

                .storeId(bill.getStore() != null ? bill.getStore().getId() : null)
                .storeName(bill.getStore() != null ? bill.getStore().getName() : null)

                .assignedStoreId(bill.getAssignedStore() != null ? bill.getAssignedStore().getId() : null)
                .assignedStoreName(bill.getAssignedStore() != null ? bill.getAssignedStore().getName() : null)

                .verificationStoreId(bill.getVerificationStore() != null ? bill.getVerificationStore().getId() : null)
                .verificationStoreName(bill.getVerificationStore() != null ? bill.getVerificationStore().getName() : null)

                .dropStoreId(bill.getDropStore() != null ? bill.getDropStore().getId() : null)
                .dropStoreName(bill.getDropStore() != null ? bill.getDropStore().getName() : null)

                .pickupAddress(bill.getPickupAddress())
                .pickupLatitude(bill.getPickupLatitude())
                .pickupLongitude(bill.getPickupLongitude())
                .dropLatitude(bill.getDropLatitude())
                .dropLongitude(bill.getDropLongitude())

                .subtotal(bill.getSubtotal())
                .gst(bill.getGst())
                .totalAmount(bill.getTotalAmount())
                .status(bill.getStatus())
                .items(items)
                .build();
    }
}