package com.example.purchasereportingapi.reporting.service;

import com.example.purchasereportingapi.reporting.dto.PurchasesBetweenReportRequest;
import com.example.purchasereportingapi.reporting.dto.PurchasesBetweenReportData;

/**
 * Servicio de consulta orientado a reporting.
 * Mantiene el armado de datos del reporte separado de la exportación para facilitar
 * una futura extracción del módulo a un microservicio dedicado.
 */
public interface PurchaseReportDataService {

    PurchasesBetweenReportData getPurchasesBetween(PurchasesBetweenReportRequest request);
}
