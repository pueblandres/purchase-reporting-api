package com.example.purchasereportingapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Ítem incluido en una compra")
public class PurchaseItemRequest {

    @NotNull(message = "El producto es obligatorio")
    @Positive(message = "El id de producto debe ser mayor a 0")
    @Schema(description = "Identificador del producto comprado", example = "1")
    private Long productId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    @Schema(description = "Cantidad de unidades compradas", example = "2", minimum = "1")
    private Integer quantity;
}
