package com.example.purchasereportingapi.controller;

import com.example.purchasereportingapi.exception.ApiErrorResponse;
import com.example.purchasereportingapi.report.TopProductReportResponse;
import com.example.purchasereportingapi.report.UserSpendReportResponse;
import com.example.purchasereportingapi.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Reportes", description = "Consultas agregadas para análisis de gastos y productos")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/total-spent-by-user")
    @Operation(
            summary = "Total gastado por usuario",
            description = "Devuelve el gasto acumulado de cada usuario, agrupado por usuario y ordenado según la consulta del reporte.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte generado correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = UserSpendReportResponse.class)),
                            examples = @ExampleObject(value = """
                                    [
                                      {
                                        "userId": 1,
                                        "userName": "Andrés Puebla",
                                        "totalSpent": 15700.00
                                      }
                                    ]
                                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno al generar el reporte",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public List<UserSpendReportResponse> totalSpentByUser() {
        return reportService.getTotalSpentByUser();
    }

    @GetMapping("/top-products")
    @Operation(
            summary = "Productos más comprados",
            description = "Devuelve los productos con mayor cantidad total comprada. Sirve para identificar los productos más demandados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte generado correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TopProductReportResponse.class)),
                            examples = @ExampleObject(value = """
                                    [
                                      {
                                        "productId": 1,
                                        "productName": "Yerba Mate Playadito 1kg",
                                        "totalQuantity": 24
                                      },
                                      {
                                        "productId": 2,
                                        "productName": "Alfajor Havanna Chocolate",
                                        "totalQuantity": 12
                                      }
                                    ]
                                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno al generar el reporte",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public List<TopProductReportResponse> topProducts() {
        return reportService.getTopPurchasedProducts();
    }
}
