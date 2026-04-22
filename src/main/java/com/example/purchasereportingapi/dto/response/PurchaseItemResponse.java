package com.example.purchasereportingapi.dto.response;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record PurchaseItemResponse(Long productId, String productName, Integer quantity, BigDecimal unitPrice,
                                   BigDecimal subtotal) {
}
