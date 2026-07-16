package com.example.whitefox.cache.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document
public class OtpCacheDoc {
    @Id
    private String id; // Format: "otp:auth:<phone>" or "otp:delivery:<orderId>"

    @Field
    private String otp;

    @Field
    private String role; // "CUSTOMER", "RIDER"

    @Field
    private boolean verified;

    // Use Couchbase document expiration
    @Field
    private long expiryTimestamp; 
}
