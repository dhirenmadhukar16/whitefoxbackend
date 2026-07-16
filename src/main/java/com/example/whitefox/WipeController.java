package com.example.whitefox;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class WipeController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/api/wipe-test-data")
    public String wipe() {
        try {
            jdbcTemplate.execute("DELETE FROM payments");
            jdbcTemplate.execute("DELETE FROM order_items");
            jdbcTemplate.execute("DELETE FROM customer_booking_items");
            jdbcTemplate.execute("DELETE FROM customer_bookings");
            jdbcTemplate.execute("DELETE FROM laundry_orders");
            
            // Delete the customer with phone 9958699527
            jdbcTemplate.execute("DELETE FROM customer_updates");
            jdbcTemplate.execute("DELETE FROM customer_addresses");
            jdbcTemplate.execute("DELETE FROM customers WHERE phone = '9958699527'");
            jdbcTemplate.execute("DELETE FROM users WHERE phone = '9958699527'");
            return "SUCCESS: Wiped data!";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }
}
