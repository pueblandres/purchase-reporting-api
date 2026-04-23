package com.example.purchasereportingapi.reporting.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PurchasesBetweenReportSummary {

    private Long purchaseCount;
    private BigDecimal totalAmount;
    private BigDecimal averageAmount;
    private Integer totalItems;
}
