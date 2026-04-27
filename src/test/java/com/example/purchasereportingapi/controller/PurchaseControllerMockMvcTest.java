package com.example.purchasereportingapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.purchasereportingapi.dto.response.PurchaseItemResponse;
import com.example.purchasereportingapi.dto.response.PurchaseResponse;
import com.example.purchasereportingapi.exception.GlobalExceptionHandler;
import com.example.purchasereportingapi.exception.ResourceNotFoundException;
import com.example.purchasereportingapi.service.PurchaseService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PurchaseController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class PurchaseControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PurchaseService purchaseService;

    @Test
    void shouldCreatePurchase() throws Exception {
        when(purchaseService.create(any())).thenReturn(PurchaseResponse.builder()
                .id(10L)
                .userId(1L)
                .userName("Andres Puebla")
                .total(new BigDecimal("10200.00"))
                .purchaseDate(LocalDateTime.of(2026, 4, 22, 18, 30))
                .items(List.of(
                        PurchaseItemResponse.builder()
                                .productId(1L)
                                .productName("Yerba Mate Playadito 1kg")
                                .quantity(2)
                                .unitPrice(new BigDecimal("4500.00"))
                                .subtotal(new BigDecimal("9000.00"))
                                .build()))
                .build());

        mockMvc.perform(post("/api/purchases")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": 1,
                                  "items": [
                                    {
                                      "productId": 1,
                                      "quantity": 2
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.total").value(10200.00));
    }

    @Test
    void shouldReturnValidationErrorWhenItemsAreMissing() throws Exception {
        mockMvc.perform(post("/api/purchases")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": 1,
                                  "items": []
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/purchases"))
                .andExpect(jsonPath("$.validations[0].field").value("items"));
    }

    @Test
    void shouldReturnNotFoundWhenUserOrProductDoesNotExist() throws Exception {
        when(purchaseService.create(any())).thenThrow(new ResourceNotFoundException("Usuario no encontrado con id: 1"));

        mockMvc.perform(post("/api/purchases")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": 1,
                                  "items": [
                                    {
                                      "productId": 1,
                                      "quantity": 2
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Usuario no encontrado con id: 1"))
                .andExpect(jsonPath("$.path").value("/api/purchases"));
    }
}
