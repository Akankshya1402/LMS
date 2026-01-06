package com.lms.analytics.controller;

import com.lms.analytics.dto.response.DashboardResponse;
import com.lms.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    public DashboardResponse getDashboard() {
        return analyticsService.getDashboard();
    }
}

