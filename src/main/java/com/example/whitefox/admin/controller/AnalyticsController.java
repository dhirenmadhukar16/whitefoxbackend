package com.example.whitefox.admin.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/admin-panel/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    @GetMapping("/executive-dashboard")
    public Map<String, Object> getExecutiveDashboard() {
        Map<String, Object> data = new HashMap<>();
        
        // Revenue KPI
        Map<String, Object> revenue = new HashMap<>();
        revenue.put("value", 48450);
        revenue.put("trend", 12.5);
        revenue.put("label", "Total Revenue");
        data.put("revenue", revenue);

        // Orders KPI
        Map<String, Object> orders = new HashMap<>();
        orders.put("value", 124);
        orders.put("trend", 8.2);
        orders.put("label", "Total Orders");
        data.put("orders", orders);

        // Pickups KPI
        Map<String, Object> pickups = new HashMap<>();
        pickups.put("value", 56);
        pickups.put("trend", 6.1);
        pickups.put("label", "Pickups");
        data.put("pickups", pickups);

        // Deliveries KPI
        Map<String, Object> deliveries = new HashMap<>();
        deliveries.put("value", 42);
        deliveries.put("trend", 9.7);
        deliveries.put("label", "Deliveries");
        data.put("deliveries", deliveries);
        
        // Active Riders
        data.put("activeRiders", 18);

        // Revenue Chart (Simulated 7 days)
        data.put("revenueChart", Arrays.asList(42000, 45000, 41000, 48000, 52000, 49000, 48450));
        
        // Orders Summary Pie
        Map<String, Object> ordersSummary = new HashMap<>();
        ordersSummary.put("total", 124);
        ordersSummary.put("completed", 42);
        ordersSummary.put("processing", 36);
        ordersSummary.put("pending", 24);
        ordersSummary.put("cancelled", 22);
        data.put("ordersSummary", ordersSummary);

        return data;
    }
}
