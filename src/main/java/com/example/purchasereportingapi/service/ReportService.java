package com.example.purchasereportingapi.service;

import com.example.purchasereportingapi.report.TopProductReportResponse;
import com.example.purchasereportingapi.report.UserSpendReportResponse;
import java.util.List;

public interface ReportService {

    List<UserSpendReportResponse> getTotalSpentByUser();

    List<TopProductReportResponse> getTopPurchasedProducts();
}
