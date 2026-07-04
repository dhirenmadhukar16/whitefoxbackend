package com.example.whitefox.store.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
@Builder
public class StoreEmployeeDTO {
    private UUID id;
    private String name;
    private String role;
    private String status;
    private String email;
    private String phone;
    private LocalDateTime joinedAt;
}
