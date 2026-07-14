package com.example.whitefox.customerreview.controller;



import com.example.whitefox.customerreview.dto.CreateCustomerReviewRequest;
import com.example.whitefox.customerreview.dto.CustomerReviewResponse;
import com.example.whitefox.customerreview.service.CustomerReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer-reviews")
@RequiredArgsConstructor
public class CustomerReviewController {

    private final CustomerReviewService reviewService;

    @PostMapping
    public CustomerReviewResponse createReview(
            @RequestBody CreateCustomerReviewRequest request
    ) {
        return reviewService.createReview(request);
    }

    @GetMapping("/{reviewId}")
    public CustomerReviewResponse getReview(
            @PathVariable UUID reviewId
    ) {
        return reviewService.getReview(reviewId);
    }

    @GetMapping("/customers/{customerId}")
    public List<CustomerReviewResponse> getReviewsByCustomer(
            @PathVariable UUID customerId
    ) {
        return reviewService.getReviewsByCustomer(customerId);
    }

    @GetMapping("/orders/{orderId}")
    public List<CustomerReviewResponse> getReviewsByOrder(
            @PathVariable UUID orderId
    ) {
        return reviewService.getReviewsByOrder(orderId);
    }

    @GetMapping("/stores/{storeId}")
    public List<CustomerReviewResponse> getReviewsByStore(
            @PathVariable UUID storeId
    ) {
        return reviewService.getReviewsByStore(storeId);
    }

    @GetMapping("/riders/{riderId}")
    public List<CustomerReviewResponse> getReviewsByRider(
            @PathVariable UUID riderId
    ) {
        return reviewService.getReviewsByRider(riderId);
    }

    @GetMapping("/riders/{riderId}/rating")
    public java.util.Map<String, Object> getRiderRating(
            @PathVariable UUID riderId
    ) {
        List<CustomerReviewResponse> reviews = reviewService.getReviewsByRider(riderId);
        if (reviews.isEmpty()) {
            return java.util.Map.of(
                    "rating", 0.0,
                    "totalReviews", 0
            );
        }
        
        double avg = reviews.stream()
                .filter(r -> r.getRiderRating() != null)
                .mapToInt(CustomerReviewResponse::getRiderRating)
                .average()
                .orElse(0.0);
                
        return java.util.Map.of(
                "rating", Math.round(avg * 10.0) / 10.0,
                "totalReviews", reviews.size()
        );
    }
}