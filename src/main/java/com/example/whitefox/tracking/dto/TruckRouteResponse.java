package com.example.whitefox.tracking.dto;

import com.example.whitefox.store.dto.StoreResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TruckRouteResponse {
    private StoreResponse store;
    private String routeType; // e.g. "STORE", "HQ"
    private List<GarmentResponse> garments;
}
