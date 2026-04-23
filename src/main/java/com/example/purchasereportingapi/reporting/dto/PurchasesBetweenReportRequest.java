package com.example.purchasereportingapi.reporting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Filtros de entrada para generar el reporte PDF de compras entre fechas")
public record PurchasesBetweenReportRequest(
        @NotNull(message = "La fecha desde es obligatoria")
        @Schema(description = "Fecha y hora inicial del rango a reportar", example = "2026-04-01T00:00:00")
        LocalDateTime startDate,
        @NotNull(message = "La fecha hasta es obligatoria")
        @Schema(description = "Fecha y hora final del rango a reportar", example = "2026-04-30T23:59:59")
        LocalDateTime endDate) {
}
