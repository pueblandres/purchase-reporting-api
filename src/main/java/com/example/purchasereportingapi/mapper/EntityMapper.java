package com.example.purchasereportingapi.mapper;

import com.example.purchasereportingapi.dto.response.ProductResponse;
import com.example.purchasereportingapi.dto.response.PurchaseItemResponse;
import com.example.purchasereportingapi.dto.response.PurchaseResponse;
import com.example.purchasereportingapi.dto.response.UserResponse;
import com.example.purchasereportingapi.entity.Product;
import com.example.purchasereportingapi.entity.Purchase;
import com.example.purchasereportingapi.entity.PurchaseItem;
import com.example.purchasereportingapi.entity.User;
import java.util.List;

public final class EntityMapper {

    private EntityMapper() {
    }

    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }

    public static PurchaseResponse toPurchaseResponse(Purchase purchase) {
        List<PurchaseItemResponse> itemResponses = purchase.getItems()
                .stream()
                .map(EntityMapper::toPurchaseItemResponse)
                .toList();

        return PurchaseResponse.builder()
                .id(purchase.getId())
                .userId(purchase.getUser().getId())
                .userName(purchase.getUser().getName())
                .total(purchase.getTotal())
                .purchaseDate(purchase.getPurchaseDate())
                .items(itemResponses)
                .build();
    }

    public static PurchaseItemResponse toPurchaseItemResponse(PurchaseItem item) {
        return PurchaseItemResponse.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .subtotal(item.getSubtotal())
                .build();
    }
}
