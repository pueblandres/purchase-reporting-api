package com.example.purchasereportingapi.service;

import com.example.purchasereportingapi.dto.request.CreatePurchaseRequest;
import com.example.purchasereportingapi.dto.response.PurchaseResponse;
import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseService {

    PurchaseResponse create(CreatePurchaseRequest request);

    List<PurchaseResponse> findAll();

    PurchaseResponse findById(Long id);

    List<PurchaseResponse> findBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
}
