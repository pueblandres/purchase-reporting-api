package com.example.purchasereportingapi.service.impl;

import com.example.purchasereportingapi.mapper.ReportMapper;
import com.example.purchasereportingapi.report.TopProductReportResponse;
import com.example.purchasereportingapi.report.UserSpendReportResponse;
import com.example.purchasereportingapi.service.ReportService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;

    @Override
    public List<UserSpendReportResponse> getTotalSpentByUser() {
        return reportMapper.findTotalSpentByUser();
    }

    @Override
    public List<TopProductReportResponse> getTopPurchasedProducts() {
        return reportMapper.findTopPurchasedProducts();
    }
}
