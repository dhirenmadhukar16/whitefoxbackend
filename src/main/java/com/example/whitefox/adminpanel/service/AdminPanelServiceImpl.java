package com.example.whitefox.adminpanel.service;



import com.example.whitefox.adminpanel.dto.*;
import com.example.whitefox.customeraddress.repository.CustomerAddressRepository;
import com.example.whitefox.customerbooking.enums.CustomerBookingStatus;
import com.example.whitefox.customerbooking.repository.CustomerBookingRepository;
import com.example.whitefox.customerreview.repository.CustomerReviewRepository;
import com.example.whitefox.customers.repository.CustomerRepository;
import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.enums.OrderStatus;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import com.example.whitefox.pickupbill.enums.PickupBillStatus;
import com.example.whitefox.pickupbill.repository.PickupBillRepository;
import com.example.whitefox.riders.repository.RiderRepository;
import com.example.whitefox.store.repository.StoreRepository;
import com.example.whitefox.storeemployee.repository.StoreEmployeeRepository;
import com.example.whitefox.tracking.repository.GarmentRepository;
import com.example.whitefox.customers.entity.Customer;
import com.example.whitefox.customeraddress.repository.CustomerAddressRepository;
import com.example.whitefox.customerreview.repository.CustomerReviewRepository;
import com.example.whitefox.adminpanel.dto.AdminCatalogStatsResponse;
import com.example.whitefox.garments.entity.ServiceCatalog;
import com.example.whitefox.garments.repository.ServiceCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.whitefox.adminpanel.dto.AdminSearchResponse;
import com.example.whitefox.storeemployee.entity.StoreEmployee;
import com.example.whitefox.customerbooking.entity.CustomerBooking;
import com.example.whitefox.tracking.entity.Garment;
import com.example.whitefox.riders.entity.Rider;
import java.time.LocalDate;
import java.util.List;
import com.example.whitefox.adminpanel.dto.AdminHqOperationsResponse;
import com.example.whitefox.pickupbill.entity.PickupBill;
import com.example.whitefox.store.entity.Store;
import com.example.whitefox.orders.enums.PaymentStatus;
import com.example.whitefox.customerbooking.entity.CustomerBooking;
import com.example.whitefox.adminpanel.dto.AdminTruckStatsResponse;
import com.example.whitefox.trucklogistics.enums.TruckStatus;
import com.example.whitefox.trucklogistics.enums.TruckTripStatus;
import com.example.whitefox.trucklogistics.repository.TruckRepository;
import com.example.whitefox.trucklogistics.repository.TruckTripRepository;
import com.example.whitefox.trucklogistics.repository.TruckManifestRepository;
@Service
@RequiredArgsConstructor
public class AdminPanelServiceImpl implements AdminPanelService {
    private final CustomerAddressRepository addressRepository;
    private final CustomerReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;
    private final RiderRepository riderRepository;
    private final LaundryOrderRepository orderRepository;
    private final GarmentRepository garmentRepository;
    private final PickupBillRepository pickupBillRepository;
    private final CustomerBookingRepository bookingRepository;
    private final StoreEmployeeRepository employeeRepository;
    private final com.example.whitefox.payment.repository.PaymentRepository paymentRepository;
    private final com.example.whitefox.invoice.repository.InvoiceRepository invoiceRepository;
    private final com.example.whitefox.customerreview.repository.CustomerReviewRepository customerReviewRepository;
    private final TruckRepository truckRepository;
    private final TruckTripRepository truckTripRepository;
    private final TruckManifestRepository truckManifestRepository;
    private final ServiceCatalogRepository catalogRepository;
    @Override
    public AdminHqOperationsResponse getHqOperations() {

        return AdminHqOperationsResponse.builder()

                .totalAtHq(
                        garmentRepository.findAll()
                                .stream()
                                .filter(g -> g.getStatus().name().contains("HQ")
                                        || g.getStatus().name().contains("WASHING")
                                        || g.getStatus().name().contains("IRONING")
                                        || g.getStatus().name().contains("PACKING"))
                                .count()
                )

                .receivedAtHq(
                        garmentRepository.findAll()
                                .stream()
                                .filter(g -> g.getStatus().name().contains("RECEIVED_AT_HQ"))
                                .count()
                )

                .washing(
                        garmentRepository.findAll()
                                .stream()
                                .filter(g -> g.getStatus().name().contains("WASHING"))
                                .count()
                )

                .ironing(
                        garmentRepository.findAll()
                                .stream()
                                .filter(g -> g.getStatus().name().contains("IRONING"))
                                .count()
                )

                .packing(
                        garmentRepository.findAll()
                                .stream()
                                .filter(g -> g.getStatus().name().contains("PACKING"))
                                .count()
                )

                .readyForDispatch(
                        garmentRepository.findAll()
                                .stream()
                                .filter(g -> g.getStatus().name().contains("READY"))
                                .count()
                )

                .sentBackToStore(
                        garmentRepository.findAll()
                                .stream()
                                .filter(g -> g.getStatus().name().contains("SENT_TO_STORE"))
                                .count()
                )

                .build();
    }
    @Override
    public AdminTruckStatsResponse getTruckStats() {

        return AdminTruckStatsResponse.builder()
                .totalTrucks(truckRepository.count())

                .availableTrucks(
                        truckRepository.findAll()
                                .stream()
                                .filter(t -> t.getStatus() == TruckStatus.AVAILABLE)
                                .count()
                )

                .onTripTrucks(
                        truckRepository.findAll()
                                .stream()
                                .filter(t -> t.getStatus() == TruckStatus.ON_TRIP)
                                .count()
                )

                .maintenanceTrucks(
                        truckRepository.findAll()
                                .stream()
                                .filter(t -> t.getStatus() == TruckStatus.MAINTENANCE)
                                .count()
                )

                .offlineTrucks(
                        truckRepository.findAll()
                                .stream()
                                .filter(t -> t.getStatus() == TruckStatus.OFFLINE)
                                .count()
                )

                .totalTrips(truckTripRepository.count())

                .activeTrips(
                        truckTripRepository.findAll()
                                .stream()
                                .filter(t -> t.getStatus() != TruckTripStatus.TRIP_COMPLETED)
                                .filter(t -> t.getStatus() != TruckTripStatus.CANCELLED)
                                .count()
                )

                .completedTrips(
                        truckTripRepository.findAll()
                                .stream()
                                .filter(t -> t.getStatus() == TruckTripStatus.TRIP_COMPLETED)
                                .count()
                )

                .totalManifests(truckManifestRepository.count())

                .build();
    }
    @Override
    public List<AdminSearchResponse> search(String keyword) {

        String q = keyword.toLowerCase();

        List<AdminSearchResponse> results = new java.util.ArrayList<>();

        customerRepository.findAll().forEach(customer -> {
            if ((customer.getName() != null && customer.getName().toLowerCase().contains(q)) ||
                    (customer.getPhone() != null && customer.getPhone().contains(q)) ||
                    (customer.getEmail() != null && customer.getEmail().toLowerCase().contains(q))) {

                results.add(AdminSearchResponse.builder()
                        .type("CUSTOMER")
                        .id(customer.getId())
                        .title(customer.getName())
                        .subtitle(customer.getPhone())
                        .status(customer.getActive() != null && customer.getActive() ? "ACTIVE" : "INACTIVE")
                        .extraInfo(customer.getEmail())
                        .build());
            }
        });

        riderRepository.findAll().forEach(rider -> {
            if ((rider.getName() != null && rider.getName().toLowerCase().contains(q)) ||
                    (rider.getPhone() != null && rider.getPhone().contains(q)) ||
                    (rider.getRiderCode() != null && rider.getRiderCode().toLowerCase().contains(q))) {

                results.add(AdminSearchResponse.builder()
                        .type("RIDER")
                        .id(rider.getId())
                        .title(rider.getName())
                        .subtitle(rider.getPhone())
                        .status(rider.getStatus() != null ? rider.getStatus().name() : null)
                        .extraInfo(rider.getRiderCode())
                        .build());
            }
        });

        storeRepository.findAll().forEach(store -> {
            if ((store.getName() != null && store.getName().toLowerCase().contains(q)) ||
                    (store.getStoreCode() != null && store.getStoreCode().toLowerCase().contains(q)) ||
                    (store.getPhone() != null && store.getPhone().contains(q))) {

                results.add(AdminSearchResponse.builder()
                        .type("STORE")
                        .id(store.getId())
                        .title(store.getName())
                        .subtitle(store.getPhone())
                        .status(store.getActive() != null && store.getActive() ? "ACTIVE" : "INACTIVE")
                        .extraInfo(store.getStoreCode())
                        .build());
            }
        });

        orderRepository.findAll().forEach(order -> {
            if ((order.getOrderNumber() != null && order.getOrderNumber().toLowerCase().contains(q)) ||
                    (order.getCustomer() != null && order.getCustomer().getName().toLowerCase().contains(q))) {

                results.add(AdminSearchResponse.builder()
                        .type("ORDER")
                        .id(order.getId())
                        .title(order.getOrderNumber())
                        .subtitle(order.getCustomer().getName())
                        .status(order.getStatus() != null ? order.getStatus().name() : null)
                        .extraInfo(order.getStore().getName())
                        .build());
            }
        });

        pickupBillRepository.findAll().forEach(bill -> {
            if ((bill.getBillNumber() != null && bill.getBillNumber().toLowerCase().contains(q)) ||
                    (bill.getCustomer() != null && bill.getCustomer().getName().toLowerCase().contains(q))) {

                results.add(AdminSearchResponse.builder()
                        .type("PICKUP_BILL")
                        .id(bill.getId())
                        .title(bill.getBillNumber())
                        .subtitle(bill.getCustomer().getName())
                        .status(bill.getStatus() != null ? bill.getStatus().name() : null)
                        .extraInfo(bill.getStore() != null ? bill.getStore().getName() : null)
                        .build());
            }
        });

        garmentRepository.findAll().forEach(garment -> {
            if ((garment.getStoreQrCode() != null && garment.getStoreQrCode().toLowerCase().contains(q)) ||
                    (garment.getOutingQrCode() != null && garment.getOutingQrCode().toLowerCase().contains(q)) ||
                    (garment.getItemName() != null && garment.getItemName().toLowerCase().contains(q))) {

                results.add(AdminSearchResponse.builder()
                        .type("GARMENT")
                        .id(garment.getId())
                        .title(garment.getItemName())
                        .subtitle(garment.getStoreQrCode())
                        .status(garment.getStatus() != null ? garment.getStatus().name() : null)
                        .extraInfo(garment.getOrder().getOrderNumber())
                        .build());
            }
        });

        employeeRepository.findAll().forEach(employee -> {
            if ((employee.getName() != null && employee.getName().toLowerCase().contains(q)) ||
                    (employee.getPhone() != null && employee.getPhone().contains(q)) ||
                    (employee.getEmail() != null && employee.getEmail().toLowerCase().contains(q))) {

                results.add(AdminSearchResponse.builder()
                        .type("STORE_EMPLOYEE")
                        .id(employee.getId())
                        .title(employee.getName())
                        .subtitle(employee.getPhone())
                        .status(employee.getStatus() != null ? employee.getStatus().name() : null)
                        .extraInfo(employee.getStore() != null ? employee.getStore().getName() : null)
                        .build());
            }
        });

        bookingRepository.findAll().forEach(booking -> {
            if ((booking.getCustomer() != null && booking.getCustomer().getName().toLowerCase().contains(q)) ||
                    (booking.getPickupAddress() != null && booking.getPickupAddress().toLowerCase().contains(q))) {

                results.add(AdminSearchResponse.builder()
                        .type("CUSTOMER_BOOKING")
                        .id(booking.getId())
                        .title(booking.getCustomer().getName())
                        .subtitle(booking.getPickupAddress())
                        .status(booking.getStatus() != null ? booking.getStatus().name() : null)
                        .extraInfo(booking.getPickupTimeSlot())
                        .build());
            }
        });

        return results;
    }
    @Override
    public List<AdminReviewResponse> getAllReviewsForAdmin() {

        return reviewRepository.findAll()
                .stream()
                .map(review -> AdminReviewResponse.builder()
                        .reviewId(review.getId())

                        .customerId(review.getCustomer().getId())
                        .customerName(review.getCustomer().getName())

                        .orderId(review.getOrder().getId())
                        .orderNumber(review.getOrder().getOrderNumber())

                        .storeId(
                                review.getStore() != null
                                        ? review.getStore().getId()
                                        : null
                        )
                        .storeName(
                                review.getStore() != null
                                        ? review.getStore().getName()
                                        : null
                        )

                        .riderId(
                                review.getRider() != null
                                        ? review.getRider().getId()
                                        : null
                        )
                        .riderName(
                                review.getRider() != null
                                        ? review.getRider().getName()
                                        : null
                        )

                        .storeRating(review.getStoreRating())
                        .riderRating(review.getRiderRating())
                        .overallRating(review.getOverallRating())
                        .feedback(review.getFeedback())
                        .createdAt(review.getCreatedAt())
                        .build())
                .toList();
    }
    @Override
    public AdminCatalogStatsResponse getCatalogStats() {

        List<ServiceCatalog> services = catalogRepository.findAll();

        return AdminCatalogStatsResponse.builder()
                .totalServices((long) services.size())

                .activeServices(
                        services.stream()
                                .filter(ServiceCatalog::getActive)
                                .count()
                )

                .inactiveServices(
                        services.stream()
                                .filter(s -> !s.getActive())
                                .count()
                )

                .laundryServices(
                        services.stream()
                                .filter(s -> s.getServiceType() != null)
                                .filter(s -> s.getServiceType().toUpperCase().contains("LAUNDRY"))
                                .count()
                )

                .dryCleaningServices(
                        services.stream()
                                .filter(s -> s.getServiceType() != null)
                                .filter(s -> s.getServiceType().toUpperCase().contains("DRY"))
                                .count()
                )

                .shoeCleaningServices(
                        services.stream()
                                .filter(s -> s.getServiceType() != null)
                                .filter(s -> s.getServiceType().toUpperCase().contains("SHOE"))
                                .count()
                )

                .ironingServices(
                        services.stream()
                                .filter(s -> s.getServiceType() != null)
                                .filter(s -> s.getServiceType().toUpperCase().contains("IRON"))
                                .count()
                )

                .build();
    }
    @Override
    public List<AdminBookingResponse> getAllBookingsForAdmin() {

        return bookingRepository.findAll()
                .stream()
                .map(booking -> AdminBookingResponse.builder()
                        .bookingId(booking.getId())

                        .customerId(booking.getCustomer().getId())
                        .customerName(booking.getCustomer().getName())
                        .customerPhone(booking.getCustomer().getPhone())

                        .storeId(
                                booking.getStore() != null
                                        ? booking.getStore().getId()
                                        : null
                        )
                        .storeName(
                                booking.getStore() != null
                                        ? booking.getStore().getName()
                                        : null
                        )

                        .riderId(
                                booking.getAssignedRider() != null
                                        ? booking.getAssignedRider().getId()
                                        : null
                        )
                        .riderName(
                                booking.getAssignedRider() != null
                                        ? booking.getAssignedRider().getName()
                                        : null
                        )

                        .pickupAddress(booking.getPickupAddress())
                        .pickupDate(booking.getPickupDate())
                        .pickupTimeSlot(booking.getPickupTimeSlot())
                        .estimatedAmount(booking.getEstimatedAmount())
                        .status(booking.getStatus())
                        .build())
                .toList();
    }
    @Override
    public AdminFinanceStatsResponse getFinanceStats() {

        List<LaundryOrder> orders = orderRepository.findAll();

        LocalDate today = LocalDate.now();

        double totalRevenue = orders.stream()
                .filter(o -> o.getTotalAmount() != null)
                .filter(o -> o.getPaymentStatus() == PaymentStatus.PAID)
                .mapToDouble(LaundryOrder::getTotalAmount)
                .sum();

        double todayRevenue = orders.stream()
                .filter(o -> o.getTotalAmount() != null)
                .filter(o -> o.getPaymentStatus() == PaymentStatus.PAID)
                .filter(o -> o.getCreatedAt() != null)
                .filter(o -> o.getCreatedAt().toLocalDate().equals(today))
                .mapToDouble(LaundryOrder::getTotalAmount)
                .sum();

        double totalOrderAmount = orders.stream()
                .filter(o -> o.getTotalAmount() != null)
                .mapToDouble(LaundryOrder::getTotalAmount)
                .sum();

        double pendingAmount = totalOrderAmount - totalRevenue;

        return AdminFinanceStatsResponse.builder()
                .totalRevenue(totalRevenue)
                .todayRevenue(todayRevenue)
                .totalPayments(paymentRepository.count())
                .paidOrders(
                        orders.stream()
                                .filter(o -> o.getPaymentStatus() == PaymentStatus.PAID)
                                .count()
                )
                .unpaidOrders(
                        orders.stream()
                                .filter(o -> o.getPaymentStatus() == PaymentStatus.UNPAID)
                                .count()
                )
                .partialPaidOrders(
                        orders.stream()
                                .filter(o -> o.getPaymentStatus() == PaymentStatus.PARTIAL)
                                .count()
                )
                .pendingAmount(pendingAmount)
                .totalInvoices(invoiceRepository.count())
                .build();
    }
    @Override
    public AdminGarmentStatsResponse getGarmentStats() {

        return AdminGarmentStatsResponse.builder()
                .totalGarments(garmentRepository.count())

                .atStore(
                        garmentRepository.findAll()
                                .stream()
                                .filter(g -> g.getStatus().name().contains("STORE"))
                                .count()
                )

                .sentToHq(
                        garmentRepository.findAll()
                                .stream()
                                .filter(g -> g.getStatus().name().contains("SENT_TO_HQ"))
                                .count()
                )

                .receivedAtHq(
                        garmentRepository.findAll()
                                .stream()
                                .filter(g -> g.getStatus().name().contains("RECEIVED_AT_HQ"))
                                .count()
                )

                .washing(
                        garmentRepository.findAll()
                                .stream()
                                .filter(g -> g.getStatus().name().contains("WASHING"))
                                .count()
                )

                .ironing(
                        garmentRepository.findAll()
                                .stream()
                                .filter(g -> g.getStatus().name().contains("IRONING"))
                                .count()
                )

                .packing(
                        garmentRepository.findAll()
                                .stream()
                                .filter(g -> g.getStatus().name().contains("PACKING"))
                                .count()
                )

                .ready(
                        garmentRepository.findAll()
                                .stream()
                                .filter(g -> g.getStatus().name().contains("READY"))
                                .count()
                )

                .delivered(
                        garmentRepository.findAll()
                                .stream()
                                .filter(g -> g.getStatus().name().contains("DELIVERED"))
                                .count()
                )

                .build();
    }
    @Override
    public List<AdminCustomerStatsResponse> getCustomerStats() {

        List<Customer> customers = customerRepository.findAll();

        return customers.stream()
                .map(customer -> {

                    List<LaundryOrder> customerOrders = orderRepository.findAll()
                            .stream()
                            .filter(o -> o.getCustomer().getId().equals(customer.getId()))
                            .toList();

                    double totalSpend = customerOrders.stream()
                            .filter(o -> o.getTotalAmount() != null)
                            .mapToDouble(LaundryOrder::getTotalAmount)
                            .sum();

                    return AdminCustomerStatsResponse.builder()
                            .customerId(customer.getId())
                            .customerName(customer.getName())
                            .phone(customer.getPhone())
                            .email(customer.getEmail())
                            .totalOrders((long) customerOrders.size())
                            .activeOrders(
                                    customerOrders.stream()
                                            .filter(o -> o.getStatus() != OrderStatus.DELIVERED)
                                            .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                                            .count()
                            )
                            .totalBookings(
                                    (long) bookingRepository.findByCustomerId(customer.getId()).size()
                            )
                            .savedAddresses(
                                    (long) addressRepository.findByCustomerId(customer.getId()).size()
                            )
                            .reviews(
                                    (long) reviewRepository.findByCustomerId(customer.getId()).size()
                            )
                            .totalSpend(totalSpend)
                            .build();
                })
                .toList();
    }
    @Override
    public List<AdminPickupBillResponse> getAllPickupBillsForAdmin() {

        return pickupBillRepository.findAll()
                .stream()
                .map(bill -> AdminPickupBillResponse.builder()

                        .pickupBillId(bill.getId())
                        .billNumber(bill.getBillNumber())

                        .customerId(bill.getCustomer().getId())
                        .customerName(bill.getCustomer().getName())

                        .riderId(
                                bill.getRider() != null
                                        ? bill.getRider().getId()
                                        : null
                        )
                        .riderName(
                                bill.getRider() != null
                                        ? bill.getRider().getName()
                                        : null
                        )

                        .storeId(bill.getStore().getId())
                        .storeName(bill.getStore().getName())

                        // Remove these 4 fields if your entity doesn't have them yet
                        .assignedStoreId(null)
                        .assignedStoreName(null)
                        .dropStoreId(null)
                        .dropStoreName(null)

                        .pickupAddress(bill.getPickupAddress())

                        .subtotal(bill.getSubtotal())
                        .gst(bill.getGst())
                        .totalAmount(bill.getTotalAmount())

                        .status(bill.getStatus())

                        .build())
                .toList();
    }
    @Override
    public AdminDashboardResponse getDashboard() {

        List<LaundryOrder> orders = orderRepository.findAll();

        LocalDate today = LocalDate.now();

        double totalRevenue = orders.stream()
                .filter(o -> o.getTotalAmount() != null)
                .mapToDouble(LaundryOrder::getTotalAmount)
                .sum();

        double todayRevenue = orders.stream()
                .filter(o -> o.getTotalAmount() != null)
                .filter(o -> o.getCreatedAt() != null)
                .filter(o -> o.getCreatedAt().toLocalDate().equals(today))
                .mapToDouble(LaundryOrder::getTotalAmount)
                .sum();

        return AdminDashboardResponse.builder()
                .totalStores(storeRepository.count())
                .totalCustomers(customerRepository.count())
                .totalRiders(riderRepository.count())
                .totalOrders((long) orders.size())

                .activeOrders(
                        orders.stream()
                                .filter(o -> o.getStatus() != OrderStatus.DELIVERED)
                                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                                .count()
                )
                .deliveredOrders(
                        orders.stream()
                                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                                .count()
                )
                .cancelledOrders(
                        orders.stream()
                                .filter(o -> o.getStatus() == OrderStatus.CANCELLED)
                                .count()
                )

                .totalGarments(garmentRepository.count())

                .pendingPickupBills(
                        pickupBillRepository.findAll()
                                .stream()
                                .filter(p -> p.getStatus() == PickupBillStatus.SENT_TO_STORE)
                                .count()
                )

                .pendingCustomerBookings(
                        bookingRepository.findAll()
                                .stream()
                                .filter(b -> b.getStatus() == CustomerBookingStatus.REQUESTED)
                                .count()
                )

                .totalEmployees(employeeRepository.count())

                .totalRevenue(totalRevenue)
                .todayRevenue(todayRevenue)

                .todayOrders(
                        orders.stream()
                                .filter(o -> o.getCreatedAt() != null)
                                .filter(o -> o.getCreatedAt().toLocalDate().equals(today))
                                .count()
                )

                .todayBookings(
                        bookingRepository.findAll()
                                .stream()
                                .filter(b -> b.getCreatedAt() != null)
                                .filter(b -> b.getCreatedAt().toLocalDate().equals(today))
                                .count()
                )

                .build();
    }
    @Override
    public List<AdminOrderResponse> getAllOrdersForAdmin() {
        return orderRepository.findAll()
                .stream()
                .map(order -> AdminOrderResponse.builder()
                        .orderId(order.getId())
                        .orderNumber(order.getOrderNumber())

                        .customerId(order.getCustomer().getId())
                        .customerName(order.getCustomer().getName())
                        .customerPhone(order.getCustomer().getPhone())

                        .storeId(order.getStore().getId())
                        .storeName(order.getStore().getName())

                        .deliveryRiderId(
                                order.getDeliveryRider() != null
                                        ? order.getDeliveryRider().getId()
                                        : null
                        )
                        .deliveryRiderName(
                                order.getDeliveryRider() != null
                                        ? order.getDeliveryRider().getName()
                                        : null
                        )

                        .status(order.getStatus())
                        .paymentStatus(order.getPaymentStatus())
                        .subtotal(order.getSubtotal())
                        .gst(order.getGst())
                        .totalAmount(order.getTotalAmount())
                        .build())
                .toList();
    }
    @Override
    public List<AdminRiderStatsResponse> getRiderStats() {

        List<Rider> riders = riderRepository.findAll();

        return riders.stream()
                .map(rider -> AdminRiderStatsResponse.builder()
                        .riderId(rider.getId())
                        .riderName(rider.getName())
                        .riderCode(rider.getRiderCode())
                        .phone(rider.getPhone())
                        .status(rider.getStatus().name())

                        .deliveryOrders(
                                (long) orderRepository
                                        .findByDeliveryRiderId(rider.getId())
                                        .size()
                        )

                        .pickupBills(
                                pickupBillRepository.findAll()
                                        .stream()
                                        .filter(p -> p.getRider().getId().equals(rider.getId()))
                                        .count()
                        )

                        .latitude(rider.getLatitude())
                        .longitude(rider.getLongitude())
                        .build())
                .toList();
    }
    @Override
    public List<AdminStoreStatsResponse> getStoreStats() {

        List<Store> stores = storeRepository.findAll();

        return stores.stream()
                .map(store -> {

                    List<LaundryOrder> storeOrders = orderRepository.findAll()
                            .stream()
                            .filter(o -> o.getStore().getId().equals(store.getId()))
                            .toList();

                    List<PickupBill> storePickupBills = pickupBillRepository.findAll()
                            .stream()
                            .filter(p -> p.getStore().getId().equals(store.getId()))
                            .toList();

                    double revenue = storeOrders.stream()
                            .filter(o -> o.getTotalAmount() != null)
                            .mapToDouble(LaundryOrder::getTotalAmount)
                            .sum();

                    return AdminStoreStatsResponse.builder()
                            .storeId(store.getId())
                            .storeName(store.getName())

                            .totalOrders((long) storeOrders.size())

                            .activeOrders(
                                    storeOrders.stream()
                                            .filter(o -> o.getStatus() != OrderStatus.DELIVERED)
                                            .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                                            .count()
                            )

                            .deliveredOrders(
                                    storeOrders.stream()
                                            .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                                            .count()
                            )

                            .totalEmployees(
                                    (long) employeeRepository.findByStoreId(store.getId()).size()
                            )

                            .totalPickupBills((long) storePickupBills.size())

                            .pendingPickupBills(
                                    storePickupBills.stream()
                                            .filter(p -> p.getStatus()
                                                    == PickupBillStatus.SENT_TO_STORE)
                                            .count()
                            )

                            .totalRevenue(revenue)
                            .build();
                })
                .toList();
    }
}
