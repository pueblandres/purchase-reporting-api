package com.example.purchasereportingapi.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.purchasereportingapi.mapper.ReportMapper;
import com.example.purchasereportingapi.report.TopProductReportResponse;
import com.example.purchasereportingapi.report.UserSpendReportResponse;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    private ReportMapper reportMapper;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Test
    void shouldReturnTotalSpentByUserReport() {
        when(reportMapper.findTotalSpentByUser()).thenReturn(List.of(
                new UserSpendReportResponse(1L, "Andres Puebla", new BigDecimal("15700.00"))));

        List<UserSpendReportResponse> result = reportService.getTotalSpentByUser();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().userName()).isEqualTo("Andres Puebla");
        assertThat(result.getFirst().totalSpent()).isEqualByComparingTo("15700.00");
    }

    @Test
    void shouldReturnEmptyTopProductsReportWhenThereIsNoData() {
        when(reportMapper.findTopPurchasedProducts()).thenReturn(List.of());

        List<TopProductReportResponse> result = reportService.getTopPurchasedProducts();

        assertThat(result).isEmpty();
    }
}
