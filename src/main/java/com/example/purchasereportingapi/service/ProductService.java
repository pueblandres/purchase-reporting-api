package com.example.purchasereportingapi.service;

import com.example.purchasereportingapi.dto.request.CreateProductRequest;
import com.example.purchasereportingapi.dto.response.ProductResponse;
import java.util.List;

public interface ProductService {

    ProductResponse create(CreateProductRequest request);

    List<ProductResponse> findAll();

    ProductResponse findById(Long id);
}
