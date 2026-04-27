package com.example.purchasereportingapi.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.purchasereportingapi.dto.request.CreatePurchaseRequest;
import com.example.purchasereportingapi.dto.request.PurchaseItemRequest;
import com.example.purchasereportingapi.dto.response.PurchaseResponse;
import com.example.purchasereportingapi.entity.Product;
import com.example.purchasereportingapi.entity.Purchase;
import com.example.purchasereportingapi.entity.User;
import com.example.purchasereportingapi.exception.BusinessException;
import com.example.purchasereportingapi.exception.ResourceNotFoundException;
import com.example.purchasereportingapi.repository.ProductRepository;
import com.example.purchasereportingapi.repository.PurchaseRepository;
import com.example.purchasereportingapi.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceImplTest {

    @Mock
    private PurchaseRepository purchaseRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    @Test
    void shouldCreatePurchaseWithMultipleItemsAndCalculateTotal() {
        User user = User.builder().id(1L).name("Ana").email("ana@email.com").build();
        Product yerba = Product.builder().id(10L).name("Yerba").price(new BigDecimal("20.00")).build();
        Product cafe = Product.builder().id(20L).name("Cafe").price(new BigDecimal("15.00")).build();

        CreatePurchaseRequest request = new CreatePurchaseRequest();
        request.setUserId(1L);

        PurchaseItemRequest firstItem = new PurchaseItemRequest();
        firstItem.setProductId(10L);
        firstItem.setQuantity(3);

        PurchaseItemRequest secondItem = new PurchaseItemRequest();
        secondItem.setProductId(20L);
        secondItem.setQuantity(2);

        request.setItems(List.of(firstItem, secondItem));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(10L)).thenReturn(Optional.of(yerba));
        when(productRepository.findById(20L)).thenReturn(Optional.of(cafe));
        when(purchaseRepository.save(any(Purchase.class))).thenAnswer(invocation -> {
            Purchase purchase = invocation.getArgument(0);
            purchase.setId(100L);
            purchase.setPurchaseDate(LocalDateTime.of(2026, 4, 23, 12, 0));
            return purchase;
        });

        PurchaseResponse response = purchaseService.create(request);

        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.total()).isEqualByComparingTo("90.00");
        assertThat(response.items()).hasSize(2);
        assertThat(response.items().getFirst().subtotal()).isEqualByComparingTo("60.00");
        assertThat(response.items().get(1).subtotal()).isEqualByComparingTo("30.00");
    }

    @Test
    void shouldFailWhenPurchaseHasNoItems() {
        CreatePurchaseRequest request = new CreatePurchaseRequest();
        request.setUserId(1L);
        request.setItems(List.of());

        assertThatThrownBy(() -> purchaseService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("La compra debe tener al menos un producto");
    }

    @Test
    void shouldFailWhenUserDoesNotExist() {
        CreatePurchaseRequest request = new CreatePurchaseRequest();
        request.setUserId(1L);
        PurchaseItemRequest item = new PurchaseItemRequest();
        item.setProductId(10L);
        item.setQuantity(1);
        request.setItems(List.of(item));

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> purchaseService.create(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Usuario no encontrado con id: 1");
    }

    @Test
    void shouldFailWhenProductDoesNotExist() {
        User user = User.builder().id(1L).name("Ana").email("ana@email.com").build();
        CreatePurchaseRequest request = new CreatePurchaseRequest();
        request.setUserId(1L);

        PurchaseItemRequest item = new PurchaseItemRequest();
        item.setProductId(10L);
        item.setQuantity(1);
        request.setItems(List.of(item));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> purchaseService.create(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Producto no encontrado con id: 10");
    }
}
