package com.example.purchasereportingapi.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record PurchaseResponse(Long id, Long userId, String userName, BigDecimal total,
                               LocalDateTime purchaseDate, List<PurchaseItemResponse> items) {
}
