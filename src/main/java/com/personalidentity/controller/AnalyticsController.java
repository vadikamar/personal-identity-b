package com.personalidentity.controller;

import com.personalidentity.dto.ApiResponseDTO;
import com.personalidentity.dto.AnalyticsOverviewDTO;
import com.personalidentity.dto.ProfileAnalyticsDTO;
import com.personalidentity.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Logger;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private final Logger log = Logger.getLogger(AnalyticsController.class.getName());
    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/overview")
    public ResponseEntity<ApiResponseDTO<AnalyticsOverviewDTO>> getOverview() {
        log.info("ProfileController getOverview");
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Analytics overview fetched", UUID.randomUUID().toString(), analyticsService.getOverview()));
    }

    @GetMapping("/profiles/{profileId}")
    public ResponseEntity<ApiResponseDTO<ProfileAnalyticsDTO>> getProfileAnalytics(@PathVariable String profileId) {
        log.info("ProfileController getProfileAnalytics");
        return analyticsService.getProfileAnalytics(profileId)
                .map(metrics -> ResponseEntity.ok(new ApiResponseDTO<>(200, "Profile analytics fetched", UUID.randomUUID().toString(), metrics)))
                .orElse(ResponseEntity.notFound().build());
    }
}
