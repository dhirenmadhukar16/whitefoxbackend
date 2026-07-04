package com.example.whitefox.admin.controller;

import com.example.whitefox.admin.entity.PricingRule;
import com.example.whitefox.admin.repository.PricingRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin-panel/settings")
@CrossOrigin("*")
public class SettingsController {

    @Autowired
    private PricingRuleRepository pricingRuleRepository;

    @GetMapping("/pricing-rules")
    public ResponseEntity<List<PricingRule>> getPricingRules() {
        return ResponseEntity.ok(pricingRuleRepository.findAll());
    }
}
