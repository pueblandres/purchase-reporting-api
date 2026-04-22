package com.example.purchasereportingapi.service.impl;

import com.example.purchasereportingapi.dto.request.CreateProductRequest;
import com.example.purchasereportingapi.dto.response.ProductResponse;
import com.example.purchasereportingapi.entity.Product;
import com.example.purchasereportingapi.exception.ResourceNotFoundException;
import com.example.purchasereportingapi.mapper.EntityMapper;
import com.example.purchasereportingapi.repository.ProductRepository;
import com.example.purchasereportingapi.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse create(CreateProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .build();

        return EntityMapper.toProductResponse(productRepository.save(product));
    }

    @Override
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream().map(EntityMapper::toProductResponse).toList();
    }

    @Override
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        return EntityMapper.toProductResponse(product);
    }
}
