package com.example.purchasereportingapi.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.purchasereportingapi.dto.request.CreateProductRequest;
import com.example.purchasereportingapi.dto.response.ProductResponse;
import com.example.purchasereportingapi.entity.Product;
import com.example.purchasereportingapi.exception.BusinessException;
import com.example.purchasereportingapi.exception.ResourceNotFoundException;
import com.example.purchasereportingapi.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.Optional;
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
    void shouldCreateProductWhenPriceIsValid() {
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

    @Test
    void shouldFailWhenPriceIsInvalid() {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Teclado");
        request.setPrice(BigDecimal.ZERO);

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("El precio debe ser mayor a 0");
    }

    @Test
    void shouldReturnProductWhenIdExists() {
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(Product.builder()
                        .id(1L)
                        .name("Yerba Mate Playadito 1kg")
                        .price(new BigDecimal("4500.00"))
                        .build()));

        ProductResponse response = productService.findById(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Yerba Mate Playadito 1kg");
    }

    @Test
    void shouldFailWhenProductDoesNotExist() {
        when(productRepository.findById(77L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(77L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Producto no encontrado con id: 77");
    }
}
