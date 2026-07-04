package com.example.whitefox.storeemployee.dto;



import com.example.whitefox.storeemployee.enums.StoreEmployeeRole;
import lombok.Data;

@Data
public class CreateStoreEmployeeRequest {

    private String name;

    private String phone;

    private String email;

    private StoreEmployeeRole role;
}
