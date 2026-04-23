package com.example.purchasereportingapi.report;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Reporte de total gastado agrupado por usuario")
public record UserSpendReportResponse(
        @Schema(description = "Identificador del usuario", example = "1")
        Long userId,
        @Schema(description = "Nombre completo del usuario", example = "Andrés Puebla")
        String userName,
        @Schema(description = "Monto total acumulado por el usuario en todas sus compras", example = "15700.00")
        BigDecimal totalSpent) {
}
