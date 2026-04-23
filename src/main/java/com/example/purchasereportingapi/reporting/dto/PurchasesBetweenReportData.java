package com.example.purchasereportingapi.reporting.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PurchasesBetweenReportData {

    private List<PurchasesBetweenReportRow> rows;
    private PurchasesBetweenReportSummary summary;
}
