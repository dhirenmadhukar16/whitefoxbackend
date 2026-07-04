package com.example.whitefox.admin.controller;

import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.enums.OrderStatus;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin-panel/orders")
@CrossOrigin(origins = "*")
public class AdminOrderController {

    @Autowired
    private LaundryOrderRepository orderRepository;

    @GetMapping("/kanban")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getKanbanOrders() {
        List<LaundryOrder> allOrders = orderRepository.findAll();
        
        Map<String, List<Map<String, Object>>> kanbanBoard = new HashMap<>();
        
        // Initialize columns
        kanbanBoard.put("Created", List.of());
        kanbanBoard.put("Pickup Assigned", List.of());
        kanbanBoard.put("Picked", List.of());
        kanbanBoard.put("Out for Delivery", List.of());
        kanbanBoard.put("Delivered", List.of());

        // Group by status
        for (OrderStatus status : OrderStatus.values()) {
            List<Map<String, Object>> mappedOrders = allOrders.stream()
                .filter(o -> o.getStatus() == status)
                .map(o -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", o.getOrderNumber() != null ? o.getOrderNumber() : "ORD-" + o.getId().toString());
                    map.put("customer", o.getCustomer() != null ? o.getCustomer().getName() : "Unknown");
                    map.put("items", 0); // Placeholder until order items relationship is verified
                    map.put("amount", o.getTotalAmount());
                    return map;
                })
                .collect(Collectors.toList());

            // Map Backend Statuses to Kanban Columns
            if (status.name().equals("CREATED")) {
                kanbanBoard.put("Created", mergeLists(kanbanBoard.get("Created"), mappedOrders));
            } else if (status.name().equals("PICKUP_ASSIGNED")) {
                kanbanBoard.put("Pickup Assigned", mergeLists(kanbanBoard.get("Pickup Assigned"), mappedOrders));
            } else if (status.name().equals("PICKED_UP") || status.name().equals("PROCESSING") || status.name().equals("READY_FOR_DELIVERY")) {
                kanbanBoard.put("Picked", mergeLists(kanbanBoard.get("Picked"), mappedOrders));
            } else if (status.name().equals("OUT_FOR_DELIVERY")) {
                kanbanBoard.put("Out for Delivery", mergeLists(kanbanBoard.get("Out for Delivery"), mappedOrders));
            } else if (status.name().equals("DELIVERED")) {
                kanbanBoard.put("Delivered", mergeLists(kanbanBoard.get("Delivered"), mappedOrders));
            }
        }
        
        return ResponseEntity.ok(kanbanBoard);
    }
    
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable java.util.UUID orderId, @RequestBody Map<String, String> body) {
        LaundryOrder order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        String newStatusStr = body.get("status");
        
        // Map Kanban string to OrderStatus enum
        OrderStatus newStatus = OrderStatus.CREATED; // default
        if (newStatusStr.equalsIgnoreCase("Created")) newStatus = OrderStatus.CREATED;
        else if (newStatusStr.equalsIgnoreCase("Pickup Assigned")) newStatus = OrderStatus.PICKUP_ASSIGNED;
        else if (newStatusStr.equalsIgnoreCase("Picked")) newStatus = OrderStatus.PROCESSING;
        else if (newStatusStr.equalsIgnoreCase("Out for Delivery")) newStatus = OrderStatus.OUT_FOR_DELIVERY;
        else if (newStatusStr.equalsIgnoreCase("Delivered")) newStatus = OrderStatus.DELIVERED;
        
        order.setStatus(newStatus);
        orderRepository.save(order);
        return ResponseEntity.ok().build();
    }

    private <T> List<T> mergeLists(List<T> list1, List<T> list2) {
        List<T> result = new java.util.ArrayList<>(list1);
        result.addAll(list2);
        return result;
    }
}
