package com.example.whitefox.store.service;

import com.example.whitefox.customers.entity.Customer;
import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import com.example.whitefox.store.dto.StoreEmployeeDTO;
import com.example.whitefox.store.dto.StoreInventoryDTO;
import com.example.whitefox.store.entity.StoreFinanceRecord;
import com.example.whitefox.store.entity.StoreInventoryItem;
import com.example.whitefox.store.repository.StoreFinanceRecordRepository;
import com.example.whitefox.store.repository.StoreInventoryItemRepository;
import com.example.whitefox.storeemployee.entity.StoreEmployee;
import com.example.whitefox.storeemployee.repository.StoreEmployeeRepository;
import com.example.whitefox.riders.entity.Rider;
import com.example.whitefox.riders.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreDetailsService {

    private final LaundryOrderRepository orderRepository;
    private final StoreEmployeeRepository employeeRepository;
    private final StoreInventoryItemRepository inventoryRepository;
    private final StoreFinanceRecordRepository financeRepository;
    private final RiderRepository riderRepository;

    public List<LaundryOrder> getStoreOrders(UUID storeId) {
        return orderRepository.findByStoreId(storeId);
    }

    public List<StoreEmployeeDTO> getStoreEmployees(UUID storeId) {
        List<StoreEmployee> employees = employeeRepository.findByStoreId(storeId);
        List<StoreEmployeeDTO> dtos = employees.stream().map(emp -> StoreEmployeeDTO.builder()
                .id(emp.getId())
                .name(emp.getName())
                .role(emp.getRole() != null ? emp.getRole().name() : "STAFF")
                .status(emp.getStatus() != null ? emp.getStatus().name() : "ACTIVE")
                .email(emp.getEmail())
                .phone(emp.getPhone())
                .joinedAt(emp.getCreatedAt())
                .build()
        ).collect(Collectors.toList());

        List<Rider> riders = riderRepository.findByStoreId(storeId);
        List<StoreEmployeeDTO> riderDtos = riders.stream().map(rider -> StoreEmployeeDTO.builder()
                .id(rider.getId())
                .name(rider.getName())
                .role("RIDER")
                .status(rider.getStatus() != null ? rider.getStatus().name() : "ACTIVE")
                .email(rider.getEmail())
                .phone(rider.getPhone())
                .joinedAt(rider.getCreatedAt())
                .build()
        ).collect(Collectors.toList());

        dtos.addAll(riderDtos);
        return dtos;
    }

    public List<StoreInventoryDTO> getStoreInventory(UUID storeId) {
        List<StoreInventoryItem> items = inventoryRepository.findByStoreId(storeId);
        return items.stream().map(item -> StoreInventoryDTO.builder()
                .id(item.getId())
                .itemName(item.getItemName())
                .category(item.getCategory())
                .quantity(item.getQuantity())
                .minimumThreshold(item.getMinimumThreshold())
                .unit(item.getUnit())
                .lastRestocked(item.getLastRestocked())
                .build()
        ).collect(Collectors.toList());
    }

    public List<Customer> getStoreCustomers(UUID storeId) {
        List<LaundryOrder> orders = orderRepository.findByStoreId(storeId);
        Set<Customer> uniqueCustomers = new HashSet<>();
        for (LaundryOrder order : orders) {
            if (order.getCustomer() != null) {
                uniqueCustomers.add(order.getCustomer());
            }
        }
        return new ArrayList<>(uniqueCustomers);
    }

    public StoreFinanceRecord getStoreFinanceForToday(UUID storeId) {
        Optional<StoreFinanceRecord> todayRecord = financeRepository.findByStoreIdAndRecordDate(storeId, LocalDate.now());
        if (todayRecord.isPresent()) {
            return todayRecord.get();
        }
        
        // Return dummy/empty record for today if none exists to avoid errors on frontend
        return StoreFinanceRecord.builder()
                .totalRevenue(0.0)
                .totalExpenses(0.0)
                .pendingBills(0.0)
                .totalOrders(0)
                .recordDate(LocalDate.now())
                .build();
    }
    
    public Map<String, Object> getStoreReports(UUID storeId) {
        List<LaundryOrder> orders = orderRepository.findByStoreId(storeId);
        Map<String, Object> reports = new HashMap<>();
        reports.put("totalOrders", orders.size());
        
        long completedOrders = orders.stream().filter(o -> "COMPLETED".equals(o.getStatus() != null ? o.getStatus().name() : "")).count();
        reports.put("completedOrders", completedOrders);
        
        double totalRevenue = orders.stream().mapToDouble(o -> o.getSubtotal() != null ? o.getSubtotal() : 0.0).sum();
        reports.put("totalRevenue", totalRevenue);
        
        int performanceScore = 0;
        if (!orders.isEmpty()) {
            performanceScore = (int) (((double) completedOrders / orders.size()) * 100);
        }
        reports.put("performanceScore", performanceScore);
        
        return reports;
    }
}
