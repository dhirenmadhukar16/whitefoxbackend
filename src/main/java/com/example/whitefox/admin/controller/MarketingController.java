package com.example.whitefox.admin.controller;

import com.example.whitefox.admin.entity.MarketingCampaign;
import com.example.whitefox.admin.repository.MarketingCampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin-panel/marketing")
@CrossOrigin("*")
public class MarketingController {

    @Autowired
    private MarketingCampaignRepository campaignRepository;

    @GetMapping("/campaigns")
    public ResponseEntity<List<MarketingCampaign>> getAllCampaigns() {
        return ResponseEntity.ok(campaignRepository.findAll());
    }

    @PostMapping("/campaigns")
    public ResponseEntity<MarketingCampaign> createCampaign(@RequestBody MarketingCampaign campaign) {
        return ResponseEntity.ok(campaignRepository.save(campaign));
    }
}
