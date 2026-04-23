package com.example.purchasereportingapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Usuario registrado en la API")
public record UserResponse(
        @Schema(description = "Identificador único del usuario", example = "1")
        Long id,
        @Schema(description = "Nombre completo del usuario", example = "Andrés Puebla")
        String name,
        @Schema(description = "Correo electrónico del usuario", example = "andy@email.com")
        String email) {
}
