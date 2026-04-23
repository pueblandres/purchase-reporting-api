package com.example.purchasereportingapi.reporting.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PurchasesBetweenReportRow {

    private Long purchaseId;
    private LocalDateTime purchaseDate;
    private String purchaseDateText;
    private Long userId;
    private String userName;
    private Integer itemCount;
    private String itemsDetail;
    private BigDecimal total;
    private String totalText;
}
