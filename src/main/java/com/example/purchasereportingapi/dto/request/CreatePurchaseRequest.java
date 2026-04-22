package com.example.purchasereportingapi.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePurchaseRequest {

    @NotNull(message = "El id del usuario es obligatorio")
    private Long userId;

    @Valid
    @NotEmpty(message = "La compra debe tener al menos un item")
    private List<PurchaseItemRequest> items;
}
