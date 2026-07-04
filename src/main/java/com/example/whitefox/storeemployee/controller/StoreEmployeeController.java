package com.example.whitefox.storeemployee.controller;

//package com.example.whitefox.storeemployee.controller;

import com.example.whitefox.storeemployee.dto.*;
import com.example.whitefox.storeemployee.service.StoreEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class StoreEmployeeController {

    private final StoreEmployeeService employeeService;

    @PostMapping("/stores/{storeId}/employees")
    public StoreEmployeeResponse createEmployee(
            @PathVariable UUID storeId,
            @RequestBody CreateStoreEmployeeRequest request
    ) {
        return employeeService.createEmployee(storeId, request);
    }

    @GetMapping("/stores/{storeId}/employees")
    public List<StoreEmployeeResponse> getEmployeesByStore(
            @PathVariable UUID storeId
    ) {
        return employeeService.getEmployeesByStore(storeId);
    }

    @GetMapping("/store-employees/{employeeId}")
    public StoreEmployeeResponse getEmployee(
            @PathVariable UUID employeeId
    ) {
        return employeeService.getEmployee(employeeId);
    }

    @PutMapping("/store-employees/{employeeId}")
    public StoreEmployeeResponse updateEmployee(
            @PathVariable UUID employeeId,
            @RequestBody UpdateStoreEmployeeRequest request
    ) {
        return employeeService.updateEmployee(employeeId, request);
    }

    @PatchMapping("/store-employees/{employeeId}/deactivate")
    public ResponseEntity<Void> deactivateEmployee(
            @PathVariable UUID employeeId
    ) {
        employeeService.deactivateEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }
}
