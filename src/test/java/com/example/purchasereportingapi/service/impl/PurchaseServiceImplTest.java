package com.example.purchasereportingapi.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.purchasereportingapi.dto.request.CreatePurchaseRequest;
import com.example.purchasereportingapi.dto.request.PurchaseItemRequest;
import com.example.purchasereportingapi.dto.response.PurchaseResponse;
import com.example.purchasereportingapi.entity.Product;
import com.example.purchasereportingapi.entity.Purchase;
import com.example.purchasereportingapi.entity.User;
import com.example.purchasereportingapi.repository.ProductRepository;
import com.example.purchasereportingapi.repository.PurchaseRepository;
import com.example.purchasereportingapi.repository.UserRepository;
import java.math.BigDecimal;
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
    void shouldCreatePurchaseAndCalculateTotal() {
        User user = User.builder().id(1L).name("Ana").email("ana@email.com").build();
        Product product = Product.builder().id(10L).name("Mouse").price(new BigDecimal("20.00")).build();

        CreatePurchaseRequest request = new CreatePurchaseRequest();
        request.setUserId(1L);
        PurchaseItemRequest itemRequest = new PurchaseItemRequest();
        itemRequest.setProductId(10L);
        itemRequest.setQuantity(3);
        request.setItems(List.of(itemRequest));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(purchaseRepository.save(org.mockito.ArgumentMatchers.any(Purchase.class)))
                .thenAnswer(invocation -> {
                    Purchase purchase = invocation.getArgument(0);
                    purchase.setId(100L);
                    return purchase;
                });

        PurchaseResponse response = purchaseService.create(request);

        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.total()).isEqualByComparingTo("60.00");
        assertThat(response.items()).hasSize(1);
    }
}
