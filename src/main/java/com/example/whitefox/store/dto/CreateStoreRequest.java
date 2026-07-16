package com.example.whitefox.store.dto;




import lombok.Data;

@Data
public class CreateStoreRequest {

    private String storeCode;

    private String name;

    private String storeAdminName;

    private String phone;

    private String email;

    private String address;

    private String city;

    private Double latitude;

    private Double longitude;

    private String password;
}