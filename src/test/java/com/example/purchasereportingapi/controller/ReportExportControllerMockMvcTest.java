package com.example.purchasereportingapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.purchasereportingapi.exception.GlobalExceptionHandler;
import com.example.purchasereportingapi.reporting.dto.PurchasesBetweenReportData;
import com.example.purchasereportingapi.reporting.dto.PurchasesBetweenReportRow;
import com.example.purchasereportingapi.reporting.dto.PurchasesBetweenReportSummary;
import com.example.purchasereportingapi.reporting.service.JasperReportExportService;
import com.example.purchasereportingapi.reporting.service.PurchaseReportDataService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReportExportController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class ReportExportControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PurchaseReportDataService purchaseReportDataService;

    @MockBean
    private JasperReportExportService jasperReportExportService;

    @Test
    void shouldReturnPdfReport() throws Exception {
        PurchasesBetweenReportRow row = new PurchasesBetweenReportRow(
                10L,
                LocalDateTime.of(2026, 4, 22, 18, 30),
                "22/04/2026 18:30",
                1L,
                "Andres Puebla",
                3,
                "- Yerba Mate Playadito 1kg x2",
                new BigDecimal("10200.00"),
                "$ 10.200,00");

        PurchasesBetweenReportData reportData = new PurchasesBetweenReportData(
                List.of(row),
                new PurchasesBetweenReportSummary(1L, new BigDecimal("10200.00"), new BigDecimal("10200.00"), 3));

        when(purchaseReportDataService.getPurchasesBetween(any())).thenReturn(reportData);
        when(jasperReportExportService.exportPdf(eq("reports/purchases-between-report.jrxml"), anyMap(), anyCollection()))
                .thenReturn("pdf-demo".getBytes());

        mockMvc.perform(post("/api/reports/purchases-between/pdf")
                        .contentType(APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_PDF)
                        .content("""
                                {
                                  "startDate": "2026-04-01T00:00:00",
                                  "endDate": "2026-04-30T23:59:59"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString(".pdf")))
                .andExpect(content().bytes("pdf-demo".getBytes()));
    }

    @Test
    void shouldReturnBadRequestWhenJsonCannotBeRead() throws Exception {
        mockMvc.perform(post("/api/reports/purchases-between/pdf")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "startDate": "fecha-invalida",
                                  "endDate": "2026-04-30T23:59:59"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("El cuerpo de la solicitud no es válido o tiene un formato JSON incorrecto"))
                .andExpect(jsonPath("$.path").value("/api/reports/purchases-between/pdf"));
    }
}
