package com.example.purchasereportingapi.mapper;

import com.example.purchasereportingapi.report.TopProductReportResponse;
import com.example.purchasereportingapi.report.UserSpendReportResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReportMapper {

    List<UserSpendReportResponse> findTotalSpentByUser();

    List<TopProductReportResponse> findTopPurchasedProducts();
}
