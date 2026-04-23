package com.example.purchasereportingapi.dto.request;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos necesarios para registrar una compra")
public class CreatePurchaseRequest {

    @NotNull(message = "El usuario es obligatorio")
    @Positive(message = "El id de usuario debe ser mayor a 0")
    @Schema(description = "Identificador del usuario que realizó la compra", example = "1")
    private Long userId;

    @Valid
    @NotEmpty(message = "La compra debe tener al menos un producto")
    @ArraySchema(schema = @Schema(description = "Producto comprado y cantidad solicitada"))
    private List<PurchaseItemRequest> items;
}
