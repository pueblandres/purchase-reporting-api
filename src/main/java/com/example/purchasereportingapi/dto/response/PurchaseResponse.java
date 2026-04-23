package com.example.purchasereportingapi.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
@Schema(description = "Compra registrada con sus productos, totales y fecha de registro")
public record PurchaseResponse(
        @Schema(description = "Identificador único de la compra", example = "10")
        Long id,
        @Schema(description = "Identificador del usuario que realizó la compra", example = "1")
        Long userId,
        @Schema(description = "Nombre del usuario que realizó la compra", example = "Andrés Puebla")
        String userName,
        @Schema(description = "Importe total de la compra", example = "10200.00")
        BigDecimal total,
        @Schema(description = "Fecha y hora en que se registró la compra", example = "2026-04-22T18:30:00")
        LocalDateTime purchaseDate,
        @ArraySchema(schema = @Schema(description = "Ítems incluidos en la compra"))
        List<PurchaseItemResponse> items) {
}
