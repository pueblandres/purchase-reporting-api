package com.example.purchasereportingapi.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.purchasereportingapi.dto.request.CreateProductRequest;
import com.example.purchasereportingapi.dto.response.ProductResponse;
import com.example.purchasereportingapi.entity.Product;
import com.example.purchasereportingapi.repository.ProductRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void shouldCreateProduct() {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Teclado");
        request.setPrice(new BigDecimal("45.50"));

        when(productRepository.save(any(Product.class))).thenReturn(Product.builder()
                .id(2L)
                .name("Teclado")
                .price(new BigDecimal("45.50"))
                .build());

        ProductResponse response = productService.create(request);

        assertThat(response.id()).isEqualTo(2L);
        assertThat(response.price()).isEqualByComparingTo("45.50");
    }
}
