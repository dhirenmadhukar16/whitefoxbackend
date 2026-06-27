package com.example.whitefox.customerreview.service;



import com.example.whitefox.customerreview.dto.CreateCustomerReviewRequest;
import com.example.whitefox.customerreview.dto.CustomerReviewResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerReviewService {

    CustomerReviewResponse createReview(CreateCustomerReviewRequest request);

    CustomerReviewResponse getReview(UUID reviewId);

    List<CustomerReviewResponse> getReviewsByCustomer(UUID customerId);

    List<CustomerReviewResponse> getReviewsByOrder(UUID orderId);

    List<CustomerReviewResponse> getReviewsByStore(UUID storeId);

    List<CustomerReviewResponse> getReviewsByRider(UUID riderId);
}
