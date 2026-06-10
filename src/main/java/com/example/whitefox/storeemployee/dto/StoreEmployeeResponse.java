package com.example.whitefox.storeemployee.dto;



import com.example.whitefox.storeemployee.enums.StoreEmployeeRole;
import com.example.whitefox.storeemployee.enums.StoreEmployeeStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class StoreEmployeeResponse {

    private UUID id;

    private UUID storeId;

    private String storeName;

    private String name;

    private String phone;

    private String email;

    private StoreEmployeeRole role;

    private StoreEmployeeStatus status;

    private Boolean active;
}
