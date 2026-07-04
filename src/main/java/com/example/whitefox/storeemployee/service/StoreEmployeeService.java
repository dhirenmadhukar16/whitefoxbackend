package com.example.whitefox.storeemployee.service;



import com.example.whitefox.storeemployee.dto.*;

import java.util.List;
import java.util.UUID;

public interface StoreEmployeeService {

    StoreEmployeeResponse createEmployee(
            UUID storeId,
            CreateStoreEmployeeRequest request
    );

    List<StoreEmployeeResponse> getEmployeesByStore(
            UUID storeId
    );

    StoreEmployeeResponse getEmployee(UUID employeeId);

    StoreEmployeeResponse updateEmployee(
            UUID employeeId,
            UpdateStoreEmployeeRequest request
    );

    void deactivateEmployee(UUID employeeId);
}
