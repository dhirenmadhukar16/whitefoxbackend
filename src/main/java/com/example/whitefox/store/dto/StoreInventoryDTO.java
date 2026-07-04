package com.example.whitefox.store.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
@Builder
public class StoreInventoryDTO {
    private UUID id;
    private String itemName;
    private String category;
    private Integer quantity;
    private Integer minimumThreshold;
    private String unit;
    private LocalDateTime lastRestocked;
}
