package com.example.whitefox.storeemployee.service;



import com.example.whitefox.store.entity.Store;
import com.example.whitefox.store.repository.StoreRepository;
import com.example.whitefox.storeemployee.dto.*;
import com.example.whitefox.storeemployee.entity.StoreEmployee;
import com.example.whitefox.storeemployee.enums.StoreEmployeeStatus;
import com.example.whitefox.storeemployee.repository.StoreEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreEmployeeServiceImpl
        implements StoreEmployeeService {

    private final StoreEmployeeRepository employeeRepository;
    private final StoreRepository storeRepository;

    @Override
    public StoreEmployeeResponse createEmployee(
            UUID storeId,
            CreateStoreEmployeeRequest request
    ) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() ->
                        new RuntimeException("Store not found"));

        StoreEmployee employee = StoreEmployee.builder()
                .store(store)
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .role(request.getRole())
                .build();

        StoreEmployee saved =
                employeeRepository.save(employee);

        return map(saved);
    }

    @Override
    public List<StoreEmployeeResponse> getEmployeesByStore(
            UUID storeId
    ) {
        return employeeRepository.findByStoreId(storeId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public StoreEmployeeResponse getEmployee(UUID employeeId) {

        StoreEmployee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        return map(employee);
    }

    @Override
    public StoreEmployeeResponse updateEmployee(
            UUID employeeId,
            UpdateStoreEmployeeRequest request
    ) {
        StoreEmployee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        employee.setName(request.getName());
        employee.setPhone(request.getPhone());
        employee.setEmail(request.getEmail());
        employee.setRole(request.getRole());

        StoreEmployee saved =
                employeeRepository.save(employee);

        return map(saved);
    }

    @Override
    public void deactivateEmployee(UUID employeeId) {

        StoreEmployee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        employee.setActive(false);
        employee.setStatus(StoreEmployeeStatus.INACTIVE);

        employeeRepository.save(employee);
    }

    private StoreEmployeeResponse map(StoreEmployee employee) {

        return StoreEmployeeResponse.builder()
                .id(employee.getId())
                .storeId(employee.getStore().getId())
                .storeName(employee.getStore().getName())
                .name(employee.getName())
                .phone(employee.getPhone())
                .email(employee.getEmail())
                .role(employee.getRole())
                .status(employee.getStatus())
                .active(employee.getActive())
                .build();
    }
}
