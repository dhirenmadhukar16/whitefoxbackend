package com.example.whitefox.customerreview.service;



import com.example.whitefox.customerreview.dto.CreateCustomerReviewRequest;
import com.example.whitefox.customerreview.dto.CustomerReviewResponse;
import com.example.whitefox.customerreview.entity.CustomerReview;
import com.example.whitefox.customerreview.repository.CustomerReviewRepository;
import com.example.whitefox.customers.entity.Customer;
import com.example.whitefox.customers.repository.CustomerRepository;
import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import com.example.whitefox.riders.entity.Rider;
import com.example.whitefox.riders.repository.RiderRepository;
import com.example.whitefox.store.entity.Store;
import com.example.whitefox.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerReviewServiceImpl implements CustomerReviewService {

    private final CustomerReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final LaundryOrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final RiderRepository riderRepository;

    @Override
    public CustomerReviewResponse createReview(CreateCustomerReviewRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        LaundryOrder order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Store store = null;
        if (request.getStoreId() != null) {
            store = storeRepository.findById(request.getStoreId())
                    .orElse(null);
        }

        Rider rider = null;
        if (request.getRiderId() != null) {
            rider = riderRepository.findById(request.getRiderId())
                    .orElse(null);
        }

        CustomerReview review = CustomerReview.builder()
                .customer(customer)
                .order(order)
                .store(store)
                .rider(rider)
                .storeRating(request.getStoreRating())
                .riderRating(request.getRiderRating())
                .overallRating(request.getOverallRating())
                .feedback(request.getFeedback())
                .build();

        return map(reviewRepository.save(review));
    }

    @Override
    public CustomerReviewResponse getReview(UUID reviewId) {
        return map(getReviewEntity(reviewId));
    }

    @Override
    public List<CustomerReviewResponse> getReviewsByCustomer(UUID customerId) {
        return reviewRepository.findByCustomerId(customerId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<CustomerReviewResponse> getReviewsByOrder(UUID orderId) {
        return reviewRepository.findByOrderId(orderId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<CustomerReviewResponse> getReviewsByStore(UUID storeId) {
        return reviewRepository.findByStoreId(storeId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<CustomerReviewResponse> getReviewsByRider(UUID riderId) {
        return reviewRepository.findByRiderId(riderId)
                .stream()
                .map(this::map)
                .toList();
    }

    private CustomerReview getReviewEntity(UUID reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
    }

    private CustomerReviewResponse map(CustomerReview review) {
        return CustomerReviewResponse.builder()
                .id(review.getId())

                .customerId(review.getCustomer().getId())
                .customerName(review.getCustomer().getName())

                .orderId(review.getOrder().getId())
                .orderNumber(review.getOrder().getOrderNumber())

                .storeId(review.getStore() != null ? review.getStore().getId() : null)
                .storeName(review.getStore() != null ? review.getStore().getName() : null)

                .riderId(review.getRider() != null ? review.getRider().getId() : null)
                .riderName(review.getRider() != null ? review.getRider().getName() : null)

                .storeRating(review.getStoreRating())
                .riderRating(review.getRiderRating())
                .overallRating(review.getOverallRating())
                .feedback(review.getFeedback())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
