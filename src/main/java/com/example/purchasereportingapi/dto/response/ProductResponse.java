package com.example.purchasereportingapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
@Schema(description = "Producto disponible para registrar compras")
public record ProductResponse(
        @Schema(description = "Identificador único del producto", example = "1")
        Long id,
        @Schema(description = "Nombre comercial del producto", example = "Yerba Mate Playadito 1kg")
        String name,
        @Schema(description = "Precio de referencia del producto", example = "4500.00")
        BigDecimal price) {
}
