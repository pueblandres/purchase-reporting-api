package com.example.purchasereportingapi.reporting.service.impl;

import com.example.purchasereportingapi.dto.response.PurchaseItemResponse;
import com.example.purchasereportingapi.dto.response.PurchaseResponse;
import com.example.purchasereportingapi.exception.BusinessException;
import com.example.purchasereportingapi.reporting.dto.PurchasesBetweenReportData;
import com.example.purchasereportingapi.reporting.dto.PurchasesBetweenReportRequest;
import com.example.purchasereportingapi.reporting.dto.PurchasesBetweenReportRow;
import com.example.purchasereportingapi.reporting.dto.PurchasesBetweenReportSummary;
import com.example.purchasereportingapi.reporting.service.PurchaseReportDataService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Comparator;
import com.example.purchasereportingapi.service.PurchaseService;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseReportDataServiceImpl implements PurchaseReportDataService {

    private static final DateTimeFormatter PURCHASE_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,##0.00",
            DecimalFormatSymbols.getInstance(Locale.forLanguageTag("es-AR")));

    private final PurchaseService purchaseService;

    @Override
    public PurchasesBetweenReportData getPurchasesBetween(PurchasesBetweenReportRequest request) {
        if (request.startDate().isAfter(request.endDate())) {
            throw new BusinessException("La fecha desde no puede ser posterior a la fecha hasta");
        }

        List<PurchasesBetweenReportRow> rows = purchaseService.findBetweenDates(request.startDate(), request.endDate())
                .stream()
                .sorted(Comparator.comparing(PurchaseResponse::purchaseDate).reversed())
                .map(this::toReportRow)
                .toList();

        return new PurchasesBetweenReportData(rows, buildSummary(rows));
    }

    private PurchasesBetweenReportRow toReportRow(PurchaseResponse purchase) {
        int itemCount = purchase.items().stream()
                .mapToInt(PurchaseItemResponse::quantity)
                .sum();

        String itemsDetail = purchase.items().stream()
                .map(item -> "- " + item.productName() + " x" + item.quantity())
                .reduce((left, right) -> left + "\n" + right)
                .orElse("Sin detalle");

        return new PurchasesBetweenReportRow(
                purchase.id(),
                purchase.purchaseDate(),
                purchase.purchaseDate().format(PURCHASE_DATE_FORMAT),
                purchase.userId(),
                purchase.userName(),
                itemCount,
                itemsDetail,
                purchase.total(),
                "$ " + MONEY_FORMAT.format(purchase.total()));
    }

    private PurchasesBetweenReportSummary buildSummary(List<PurchasesBetweenReportRow> rows) {
        long purchaseCount = rows.size();
        BigDecimal totalAmount = rows.stream()
                .map(PurchasesBetweenReportRow::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalItems = rows.stream()
                .mapToInt(PurchasesBetweenReportRow::getItemCount)
                .sum();
        BigDecimal averageAmount = purchaseCount == 0
                ? BigDecimal.ZERO
                : totalAmount.divide(BigDecimal.valueOf(purchaseCount), 2, RoundingMode.HALF_UP);

        return new PurchasesBetweenReportSummary(
                purchaseCount,
                totalAmount,
                averageAmount,
                totalItems);
    }
}
