package com.example.purchasereportingapi.reporting.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.example.purchasereportingapi.dto.response.PurchaseItemResponse;
import com.example.purchasereportingapi.dto.response.PurchaseResponse;
import com.example.purchasereportingapi.exception.BusinessException;
import com.example.purchasereportingapi.reporting.dto.PurchasesBetweenReportData;
import com.example.purchasereportingapi.reporting.dto.PurchasesBetweenReportRequest;
import com.example.purchasereportingapi.service.PurchaseService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PurchaseReportDataServiceImplTest {

    @Mock
    private PurchaseService purchaseService;

    @InjectMocks
    private PurchaseReportDataServiceImpl purchaseReportDataService;

    @Test
    void shouldMapPurchasesToReportRowsAndBuildSummary() {
        PurchasesBetweenReportRequest request = new PurchasesBetweenReportRequest(
                LocalDateTime.of(2026, 4, 1, 0, 0),
                LocalDateTime.of(2026, 4, 30, 23, 59, 59));

        PurchaseResponse newerPurchase = PurchaseResponse.builder()
                .id(10L)
                .userId(1L)
                .userName("Andres Puebla")
                .purchaseDate(LocalDateTime.of(2026, 4, 22, 18, 30))
                .total(new BigDecimal("10200.00"))
                .items(List.of(
                        PurchaseItemResponse.builder()
                                .productId(1L)
                                .productName("Yerba Mate Playadito 1kg")
                                .quantity(2)
                                .unitPrice(new BigDecimal("4500.00"))
                                .subtotal(new BigDecimal("9000.00"))
                                .build(),
                        PurchaseItemResponse.builder()
                                .productId(2L)
                                .productName("Alfajor Havanna Chocolate")
                                .quantity(1)
                                .unitPrice(new BigDecimal("1200.00"))
                                .subtotal(new BigDecimal("1200.00"))
                                .build()))
                .build();

        PurchaseResponse olderPurchase = PurchaseResponse.builder()
                .id(9L)
                .userId(2L)
                .userName("Maria Gomez")
                .purchaseDate(LocalDateTime.of(2026, 4, 10, 9, 15))
                .total(new BigDecimal("5000.00"))
                .items(List.of(
                        PurchaseItemResponse.builder()
                                .productId(3L)
                                .productName("Cafe en grano 500g")
                                .quantity(4)
                                .unitPrice(new BigDecimal("1250.00"))
                                .subtotal(new BigDecimal("5000.00"))
                                .build()))
                .build();

        when(purchaseService.findBetweenDates(request.startDate(), request.endDate()))
                .thenReturn(List.of(olderPurchase, newerPurchase));

        PurchasesBetweenReportData reportData = purchaseReportDataService.getPurchasesBetween(request);

        assertThat(reportData.getRows()).hasSize(2);
        assertThat(reportData.getRows().getFirst().getPurchaseId()).isEqualTo(10L);
        assertThat(reportData.getRows().getFirst().getPurchaseDateText()).isEqualTo("22/04/2026 18:30");
        assertThat(reportData.getRows().getFirst().getItemCount()).isEqualTo(3);
        assertThat(reportData.getRows().getFirst().getItemsDetail())
                .isEqualTo("- Yerba Mate Playadito 1kg x2\n- Alfajor Havanna Chocolate x1");
        assertThat(reportData.getRows().getFirst().getTotalText()).isEqualTo("$ 10.200,00");
        assertThat(reportData.getSummary().getPurchaseCount()).isEqualTo(2L);
        assertThat(reportData.getSummary().getTotalItems()).isEqualTo(7);
        assertThat(reportData.getSummary().getTotalAmount()).isEqualByComparingTo("15200.00");
        assertThat(reportData.getSummary().getAverageAmount()).isEqualByComparingTo("7600.00");
    }

    @Test
    void shouldRejectInvalidDateRange() {
        PurchasesBetweenReportRequest request = new PurchasesBetweenReportRequest(
                LocalDateTime.of(2026, 4, 30, 23, 59, 59),
                LocalDateTime.of(2026, 4, 1, 0, 0));

        assertThatThrownBy(() -> purchaseReportDataService.getPurchasesBetween(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("La fecha desde no puede ser posterior a la fecha hasta");
    }

    @Test
    void shouldReturnEmptyRowsAndZeroSummaryWhenThereIsNoData() {
        PurchasesBetweenReportRequest request = new PurchasesBetweenReportRequest(
                LocalDateTime.of(2026, 5, 1, 0, 0),
                LocalDateTime.of(2026, 5, 31, 23, 59, 59));

        when(purchaseService.findBetweenDates(request.startDate(), request.endDate()))
                .thenReturn(List.of());

        PurchasesBetweenReportData reportData = purchaseReportDataService.getPurchasesBetween(request);

        assertThat(reportData.getRows()).isEmpty();
        assertThat(reportData.getSummary().getPurchaseCount()).isEqualTo(0L);
        assertThat(reportData.getSummary().getTotalItems()).isZero();
        assertThat(reportData.getSummary().getTotalAmount()).isEqualByComparingTo("0");
        assertThat(reportData.getSummary().getAverageAmount()).isEqualByComparingTo("0");
    }
}
