package com.example.purchasereportingapi.controller;

import com.example.purchasereportingapi.dto.request.CreatePurchaseRequest;
import com.example.purchasereportingapi.dto.response.PurchaseResponse;
import com.example.purchasereportingapi.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar compra")
    public PurchaseResponse create(@Valid @RequestBody CreatePurchaseRequest request) {
        return purchaseService.create(request);
    }

    @GetMapping
    @Operation(summary = "Listar compras")
    public List<PurchaseResponse> findAll() {
        return purchaseService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener compra por id")
    public PurchaseResponse findById(@PathVariable Long id) {
        return purchaseService.findById(id);
    }

    @GetMapping("/between-dates")
    @Operation(summary = "Listar compras entre fechas")
    public List<PurchaseResponse> findBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return purchaseService.findBetweenDates(start, end);
    }
}
