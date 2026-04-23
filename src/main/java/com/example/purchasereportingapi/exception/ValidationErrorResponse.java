package com.example.purchasereportingapi.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detalle de un error de validación asociado a un campo o parámetro")
public record ValidationErrorResponse(
        @Schema(description = "Campo o parámetro que falló la validación", example = "email")
        String field,
        @Schema(description = "Mensaje claro del error de validación", example = "El email es obligatorio")
        String message,
        @Schema(description = "Valor rechazado por la validación", example = "correo-invalido")
        Object rejectedValue) {
}
