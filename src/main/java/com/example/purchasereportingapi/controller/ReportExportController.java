package com.example.purchasereportingapi.controller;

import com.example.purchasereportingapi.exception.ApiErrorResponse;
import com.example.purchasereportingapi.reporting.dto.PurchasesBetweenReportData;
import com.example.purchasereportingapi.reporting.dto.PurchasesBetweenReportRequest;
import com.example.purchasereportingapi.reporting.service.JasperReportExportService;
import com.example.purchasereportingapi.reporting.service.PurchaseReportDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Reportes PDF", description = "Exportación de reportes PDF desacoplada de la consulta de datos")
public class ReportExportController {

    private static final String PURCHASES_BETWEEN_TEMPLATE = "reports/purchases-between-report.jrxml";
    private static final DateTimeFormatter FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final DateTimeFormatter HEADER_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HEADER_DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,##0.00",
            DecimalFormatSymbols.getInstance(Locale.forLanguageTag("es-AR")));

    private final PurchaseReportDataService purchaseReportDataService;
    private final JasperReportExportService jasperReportExportService;

    @PostMapping(value = "/purchases-between/pdf",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_PDF_VALUE)
    @Operation(
            summary = "Generar PDF de compras entre fechas",
            description = "Recibe un rango de fechas en JSON, consulta los datos desde la capa de servicio "
                    + "y genera un PDF con JasperReports. El diseño queda separado para facilitar una futura "
                    + "extracción del módulo de reporting a un microservicio.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "PDF generado correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_PDF_VALUE,
                            schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(responseCode = "400", description = "Filtros inválidos o fechas inconsistentes",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno al generar el PDF",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<byte[]> exportPurchasesBetweenPdf(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Rango de fechas a utilizar en el reporte",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PurchasesBetweenReportRequest.class),
                            examples = @ExampleObject(name = "Reporte mensual", value = """
                                    {
                                      "startDate": "2026-04-01T00:00:00",
                                      "endDate": "2026-04-30T23:59:59"
                                    }
                                    """)))
            @Valid @RequestBody PurchasesBetweenReportRequest request) {
        PurchasesBetweenReportData reportData = purchaseReportDataService.getPurchasesBetween(request);
        LocalDateTime generatedAt = LocalDateTime.now();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("reportTitle", "Reporte de compras entre fechas");
        parameters.put("startDate", request.startDate().format(HEADER_DATE_FORMAT));
        parameters.put("endDate", request.endDate().format(HEADER_DATE_FORMAT));
        parameters.put("generatedAt", generatedAt.format(HEADER_DATETIME_FORMAT));
        parameters.put("purchaseCount", reportData.getSummary().getPurchaseCount());
        parameters.put("totalItems", reportData.getSummary().getTotalItems());
        parameters.put("totalAmount", formatMoney(reportData.getSummary().getTotalAmount()));
        parameters.put("averageAmount", formatMoney(reportData.getSummary().getAverageAmount()));
        parameters.put("sortDescription", "Ordenado por fecha de compra descendente");

        byte[] pdf = jasperReportExportService.exportPdf(PURCHASES_BETWEEN_TEMPLATE, parameters, reportData.getRows());

        String filename = "reporte-compras-entre-fechas-" + generatedAt.format(FILE_DATE_FORMAT) + ".pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(filename).build().toString())
                .body(pdf);
    }

    private String formatMoney(java.math.BigDecimal amount) {
        return "$ " + MONEY_FORMAT.format(amount);
    }
}
