package com.example.purchasereportingapi.repository;

import com.example.purchasereportingapi.entity.Purchase;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByPurchaseDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
