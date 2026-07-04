package com.example.whitefox.riders.dto;




import com.example.whitefox.riders.enums.RiderStatus;
import lombok.Data;

@Data
public class UpdateRiderStatusRequest {

    private RiderStatus status;
}