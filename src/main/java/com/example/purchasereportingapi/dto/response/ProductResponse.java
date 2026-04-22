package com.example.purchasereportingapi.dto.response;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record ProductResponse(Long id, String name, BigDecimal price) {
}
