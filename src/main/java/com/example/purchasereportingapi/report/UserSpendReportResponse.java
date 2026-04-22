package com.example.purchasereportingapi.report;

import java.math.BigDecimal;

public record UserSpendReportResponse(Long userId, String userName, BigDecimal totalSpent) {
}
