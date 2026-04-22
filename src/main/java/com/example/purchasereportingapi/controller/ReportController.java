package com.example.purchasereportingapi.controller;

import com.example.purchasereportingapi.report.TopProductReportResponse;
import com.example.purchasereportingapi.report.UserSpendReportResponse;
import com.example.purchasereportingapi.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/total-spent-by-user")
    @Operation(summary = "Total gastado por usuario")
    public List<UserSpendReportResponse> totalSpentByUser() {
        return reportService.getTotalSpentByUser();
    }

    @GetMapping("/top-products")
    @Operation(summary = "Productos más comprados")
    public List<TopProductReportResponse> topProducts() {
        return reportService.getTopPurchasedProducts();
    }
}
