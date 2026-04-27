package com.example.purchasereportingapi.service.impl;

import com.example.purchasereportingapi.dto.request.CreatePurchaseRequest;
import com.example.purchasereportingapi.dto.request.PurchaseItemRequest;
import com.example.purchasereportingapi.dto.response.PurchaseResponse;
import com.example.purchasereportingapi.entity.Product;
import com.example.purchasereportingapi.entity.Purchase;
import com.example.purchasereportingapi.entity.PurchaseItem;
import com.example.purchasereportingapi.entity.User;
import com.example.purchasereportingapi.exception.BusinessException;
import com.example.purchasereportingapi.exception.ResourceNotFoundException;
import com.example.purchasereportingapi.mapper.EntityMapper;
import com.example.purchasereportingapi.repository.ProductRepository;
import com.example.purchasereportingapi.repository.PurchaseRepository;
import com.example.purchasereportingapi.repository.UserRepository;
import com.example.purchasereportingapi.service.PurchaseService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public PurchaseResponse create(CreatePurchaseRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BusinessException("La compra debe tener al menos un producto");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + request.getUserId()));

        Purchase purchase = Purchase.builder()
                .user(user)
                .build();

        List<PurchaseItem> items = request.getItems().stream()
                .map(itemRequest -> toPurchaseItem(purchase, itemRequest))
                .toList();

        purchase.setItems(items);
        purchase.calculateTotal();

        return EntityMapper.toPurchaseResponse(purchaseRepository.save(purchase));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseResponse> findAll() {
        return purchaseRepository.findAll().stream().map(EntityMapper::toPurchaseResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseResponse findById(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada con id: " + id));
        return EntityMapper.toPurchaseResponse(purchase);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseResponse> findBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return purchaseRepository.findByPurchaseDateBetween(startDate, endDate)
                .stream()
                .map(EntityMapper::toPurchaseResponse)
                .toList();
    }

    private PurchaseItem toPurchaseItem(Purchase purchase, PurchaseItemRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + request.getProductId()));

        PurchaseItem item = PurchaseItem.builder()
                .purchase(purchase)
                .product(product)
                .quantity(request.getQuantity())
                .unitPrice(product.getPrice())
                .build();
        item.calculateSubtotal();
        return item;
    }
}
