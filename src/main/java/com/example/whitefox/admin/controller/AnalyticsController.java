package com.example.whitefox.admin.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.whitefox.orders.repository.LaundryOrderRepository;
import com.example.whitefox.customerbooking.repository.CustomerBookingRepository;
import com.example.whitefox.riders.repository.RiderRepository;
import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.riders.entity.Rider;
import com.example.whitefox.orders.enums.OrderStatus;

import java.util.*;

@RestController
@RequestMapping("/api/admin-panel/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    @Autowired
    private LaundryOrderRepository laundryOrderRepository;

    @Autowired
    private CustomerBookingRepository customerBookingRepository;

    @Autowired
    private RiderRepository riderRepository;

    @GetMapping("/executive-dashboard")
    public Map<String, Object> getExecutiveDashboard() {
        Map<String, Object> data = new HashMap<>();
        
        List<LaundryOrder> allOrders = laundryOrderRepository.findAll();
        List<Rider> allRiders = riderRepository.findAll();
        long totalPickups = customerBookingRepository.count();
        
        double totalRevenueVal = 0.0;
        int completed = 0;
        int processing = 0;
        int pending = 0;
        int cancelled = 0;
        
        for (LaundryOrder order : allOrders) {
            if (order.getStatus() != OrderStatus.CANCELLED) {
                totalRevenueVal += (order.getTotalAmount() != null ? order.getTotalAmount() : 0.0);
            }
            
            if (order.getStatus() == OrderStatus.DELIVERED) {
                completed++;
            } else if (order.getStatus() == OrderStatus.CANCELLED) {
                cancelled++;
            } else if (order.getStatus() == OrderStatus.CREATED) {
                pending++;
            } else {
                processing++;
            }
        }
        
        long activeRidersCount = 0;
        for (Rider r : allRiders) {
            if (r.getStatus() != null && !r.getStatus().name().equals("OFFLINE")) {
                activeRidersCount++;
            }
        }

        // Revenue KPI
        Map<String, Object> revenue = new HashMap<>();
        revenue.put("value", Math.round(totalRevenueVal));
        revenue.put("trend", 0.0);
        revenue.put("label", "Total Revenue");
        data.put("revenue", revenue);

        // Orders KPI
        Map<String, Object> orders = new HashMap<>();
        orders.put("value", allOrders.size());
        orders.put("trend", 0.0);
        orders.put("label", "Total Orders");
        data.put("orders", orders);

        // Pickups KPI
        Map<String, Object> pickups = new HashMap<>();
        pickups.put("value", totalPickups);
        pickups.put("trend", 0.0);
        pickups.put("label", "Pickups");
        data.put("pickups", pickups);

        // Deliveries KPI (Completed orders)
        Map<String, Object> deliveries = new HashMap<>();
        deliveries.put("value", completed);
        deliveries.put("trend", 0.0);
        deliveries.put("label", "Deliveries");
        data.put("deliveries", deliveries);
        
        // Active Riders
        data.put("activeRiders", activeRidersCount);

        // Revenue Chart (Empty to clear fake data)
        data.put("revenueChart", Arrays.asList(0, 0, 0, 0, 0, 0, 0));
        
        // Orders Summary Pie
        Map<String, Object> ordersSummary = new HashMap<>();
        ordersSummary.put("total", allOrders.size());
        ordersSummary.put("completed", completed);
        ordersSummary.put("processing", processing);
        ordersSummary.put("pending", pending);
        ordersSummary.put("cancelled", cancelled);
        data.put("ordersSummary", ordersSummary);

        return data;
    }
}
