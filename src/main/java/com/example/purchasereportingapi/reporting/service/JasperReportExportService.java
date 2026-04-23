package com.example.purchasereportingapi.reporting.service;

import java.util.Collection;
import java.util.Map;

/**
 * Contrato de exportación desacoplado del origen de datos.
 * Permite reutilizar Jasper hoy y reemplazar o mover esta implementación en el futuro.
 */
public interface JasperReportExportService {

    byte[] exportPdf(String templatePath, Map<String, Object> parameters, Collection<?> rows);
}
