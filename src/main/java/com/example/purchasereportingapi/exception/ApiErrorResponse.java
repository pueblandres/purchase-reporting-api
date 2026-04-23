package com.example.purchasereportingapi.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
@Schema(description = "Respuesta estándar de error devuelta por la API")
public record ApiErrorResponse(
        @Schema(description = "Fecha y hora en que ocurrió el error", example = "2026-04-23T10:15:30")
        LocalDateTime timestamp,
        @Schema(description = "Código HTTP asociado al error", example = "400")
        int status,
        @Schema(description = "Descripción estándar del estado HTTP", example = "Bad Request")
        String error,
        @Schema(description = "Mensaje legible con el detalle del problema", example = "Error de validación")
        String message,
        @Schema(description = "Ruta del endpoint que produjo el error", example = "/api/users")
        String path,
        @Schema(description = "Errores de validación por campo cuando el request no cumple las reglas")
        List<ValidationErrorResponse> validations) {
}
