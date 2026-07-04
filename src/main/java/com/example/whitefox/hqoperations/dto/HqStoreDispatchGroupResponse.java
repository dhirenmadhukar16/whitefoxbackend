package com.example.whitefox.hqoperations.dto;

import lombok.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HqStoreDispatchGroupResponse {
    private UUID storeId;
    private String storeName;
    private Long garmentCount;
    private List<HqGarmentResponse> garments;
}