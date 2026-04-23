package com.example.purchasereportingapi.report;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Reporte de productos ordenados por cantidad total comprada")
public record TopProductReportResponse(
        @Schema(description = "Identificador del producto", example = "1")
        Long productId,
        @Schema(description = "Nombre del producto", example = "Yerba Mate Playadito 1kg")
        String productName,
        @Schema(description = "Cantidad total de unidades compradas", example = "24")
        Long totalQuantity) {
}
