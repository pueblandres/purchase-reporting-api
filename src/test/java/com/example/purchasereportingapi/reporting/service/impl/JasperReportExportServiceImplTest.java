package com.example.purchasereportingapi.reporting.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.purchasereportingapi.reporting.dto.PurchasesBetweenReportRow;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JasperReportExportServiceImplTest {

    private final JasperReportExportServiceImpl service = new JasperReportExportServiceImpl();

    @Test
    void shouldGeneratePdfFromTemplate() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("reportTitle", "Reporte de compras entre fechas");
        parameters.put("startDate", "01/04/2026");
        parameters.put("endDate", "30/04/2026");
        parameters.put("generatedAt", "23/04/2026 15:10");
        parameters.put("purchaseCount", 1L);
        parameters.put("totalItems", 3);
        parameters.put("totalAmount", "$ 10.200,00");
        parameters.put("averageAmount", "$ 10.200,00");
        parameters.put("sortDescription", "Ordenado por fecha de compra descendente");

        List<PurchasesBetweenReportRow> rows = List.of(
                new PurchasesBetweenReportRow(
                        10L,
                        LocalDateTime.of(2026, 4, 22, 18, 30),
                        "22/04/2026 18:30",
                        1L,
                        "Andres Puebla",
                        3,
                        "- Yerba Mate Playadito 1kg x2\n- Alfajor Havanna Chocolate x1",
                        new BigDecimal("10200.00"),
                        "$ 10.200,00"));

        byte[] pdf = service.exportPdf("reports/purchases-between-report.jrxml", parameters, rows);

        assertThat(pdf).isNotNull();
        assertThat(pdf.length).isGreaterThan(0);
    }
}
