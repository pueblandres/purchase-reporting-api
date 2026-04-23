package com.example.purchasereportingapi.controller;

import com.example.purchasereportingapi.dto.request.CreatePurchaseRequest;
import com.example.purchasereportingapi.dto.response.PurchaseResponse;
import com.example.purchasereportingapi.exception.ApiErrorResponse;
import com.example.purchasereportingapi.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
@Validated
@Tag(name = "Compras", description = "Registro y consulta de compras realizadas por usuarios")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Registrar compra",
            description = "Crea una compra para un usuario existente. El precio unitario se toma del precio actual de cada producto.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Compra registrada correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PurchaseResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 10,
                                      "userId": 1,
                                      "userName": "Andrés Puebla",
                                      "total": 10200.00,
                                      "purchaseDate": "2026-04-22T18:30:00",
                                      "items": [
                                        {
                                          "productId": 1,
                                          "productName": "Yerba Mate Playadito 1kg",
                                          "quantity": 2,
                                          "unitPrice": 4500.00,
                                          "subtotal": 9000.00
                                        },
                                        {
                                          "productId": 2,
                                          "productName": "Alfajor Havanna Chocolate",
                                          "quantity": 1,
                                          "unitPrice": 1200.00,
                                          "subtotal": 1200.00
                                        }
                                      ]
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Request inválido o compra sin ítems",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuario o producto inexistente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno al registrar la compra",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public PurchaseResponse create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Compra a registrar. Usá IDs existentes de usuarios y productos.",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreatePurchaseRequest.class),
                            examples = @ExampleObject(name = "Compra demo", value = """
                                    {
                                      "userId": 1,
                                      "items": [
                                        {
                                          "productId": 1,
                                          "quantity": 2
                                        },
                                        {
                                          "productId": 2,
                                          "quantity": 1
                                        }
                                      ]
                                    }
                                    """)))
            @Valid @RequestBody CreatePurchaseRequest request) {
        return purchaseService.create(request);
    }

    @GetMapping
    @Operation(
            summary = "Listar compras",
            description = "Devuelve todas las compras registradas con usuario, total, fecha e ítems.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de compras obtenido correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = PurchaseResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno al listar compras",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public List<PurchaseResponse> findAll() {
        return purchaseService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener compra por id",
            description = "Busca una compra puntual y devuelve su detalle completo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Compra encontrada",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PurchaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "No existe una compra con el id informado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "El id informado no es válido",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno al buscar la compra",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public PurchaseResponse findById(
            @Parameter(description = "Identificador de la compra", example = "10", required = true)
            @PathVariable @Positive(message = "El id debe ser mayor a 0") Long id) {
        return purchaseService.findById(id);
    }

    @GetMapping("/between-dates")
    @Operation(
            summary = "Listar compras entre fechas",
            description = "Filtra compras cuya fecha de registro esté dentro del rango indicado. "
                    + "Los valores por defecto permiten ejecutar rápido desde Swagger UI.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Compras filtradas correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = PurchaseResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Formato de fecha inválido",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno al filtrar compras",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public List<PurchaseResponse> findBetweenDates(
            @Parameter(description = "Fecha y hora inicial en formato ISO-8601",
                    example = "2026-04-01T00:00:00")
            @RequestParam(defaultValue = "2026-04-01T00:00:00")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,
            @Parameter(description = "Fecha y hora final en formato ISO-8601",
                    example = "2026-04-30T23:59:59")
            @RequestParam(defaultValue = "2026-04-30T23:59:59")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end) {
        return purchaseService.findBetweenDates(start, end);
    }
}
