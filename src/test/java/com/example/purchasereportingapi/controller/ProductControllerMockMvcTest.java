package com.example.purchasereportingapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.purchasereportingapi.dto.response.ProductResponse;
import com.example.purchasereportingapi.exception.GlobalExceptionHandler;
import com.example.purchasereportingapi.service.ProductService;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class ProductControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void shouldCreateProduct() throws Exception {
        when(productService.create(any())).thenReturn(ProductResponse.builder()
                .id(1L)
                .name("Yerba Mate Playadito 1kg")
                .price(new BigDecimal("4500.00"))
                .build());

        mockMvc.perform(post("/api/products")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Yerba Mate Playadito 1kg",
                                  "price": 4500.00
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Yerba Mate Playadito 1kg"))
                .andExpect(jsonPath("$.price").value(4500.00));
    }

    @Test
    void shouldReturnValidationErrorWhenPriceIsMissing() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Yerba Mate Playadito 1kg"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/products"))
                .andExpect(jsonPath("$.validations[0].field").value("price"))
                .andExpect(jsonPath("$.validations[0].message").value("El precio del producto es obligatorio"));
    }
}
