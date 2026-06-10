package com.example.whitefox.tracking.dto;



import lombok.Data;

@Data
public class CreateGarmentRequest {

    private String itemName;

    private String serviceType;

    private String color;

    private String conditionNote;

    private String photoUrl;
}
