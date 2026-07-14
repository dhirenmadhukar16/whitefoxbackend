package com.example.whitefox.tracking.service;

import com.example.whitefox.tracking.dto.BagResponse;
import com.example.whitefox.tracking.dto.CreateBagRequest;
import java.util.List;
import java.util.UUID;

public interface BagService {
    BagResponse createBag(CreateBagRequest request);
    BagResponse getBag(UUID bagId);
    BagResponse getBagByQrCode(String qrCode);
    List<BagResponse> getBagsBySourceStore(UUID storeId);
    BagResponse updateBagStatus(UUID bagId, String newStatus);
    BagResponse processGarmentsInBag(UUID bagId, String targetStatus);
}
