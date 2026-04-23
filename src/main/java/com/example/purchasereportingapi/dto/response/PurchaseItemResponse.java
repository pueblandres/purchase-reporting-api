package com.example.purchasereportingapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
@Schema(description = "Detalle de un producto incluido en una compra")
public record PurchaseItemResponse(
        @Schema(description = "Identificador del producto comprado", example = "1")
        Long productId,
        @Schema(description = "Nombre del producto comprado", example = "Yerba Mate Playadito 1kg")
        String productName,
        @Schema(description = "Cantidad de unidades compradas", example = "2")
        Integer quantity,
        @Schema(description = "Precio unitario aplicado al momento de la compra", example = "4500.00")
        BigDecimal unitPrice,
        @Schema(description = "Subtotal del ítem calculado como cantidad por precio unitario", example = "9000.00")
        BigDecimal subtotal) {
}
