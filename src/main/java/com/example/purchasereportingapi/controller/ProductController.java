package com.example.purchasereportingapi.controller;

import com.example.purchasereportingapi.dto.request.CreateProductRequest;
import com.example.purchasereportingapi.dto.response.ProductResponse;
import com.example.purchasereportingapi.exception.ApiErrorResponse;
import com.example.purchasereportingapi.service.ProductService;
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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
@Tag(name = "Productos", description = "Alta y consulta de productos comprables")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Crear producto",
            description = "Registra un producto con precio de referencia. Ese precio se usa como precio unitario en las compras.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "name": "Yerba Mate Playadito 1kg",
                                      "price": 4500.00
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Request inválido o precio menor o igual a cero",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto con datos existentes",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno al crear el producto",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ProductResponse create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del producto a crear",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateProductRequest.class),
                            examples = @ExampleObject(name = "Producto demo", value = """
                                    {
                                      "name": "Yerba Mate Playadito 1kg",
                                      "price": 4500.00
                                    }
                                    """)))
            @Valid @RequestBody CreateProductRequest request) {
        return productService.create(request);
    }

    @GetMapping
    @Operation(
            summary = "Listar productos",
            description = "Devuelve todos los productos disponibles para armar compras desde Swagger UI.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de productos obtenido correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class)),
                            examples = @ExampleObject(value = """
                                    [
                                      {
                                        "id": 1,
                                        "name": "Yerba Mate Playadito 1kg",
                                        "price": 4500.00
                                      },
                                      {
                                        "id": 2,
                                        "name": "Alfajor Havanna Chocolate",
                                        "price": 1200.00
                                      }
                                    ]
                                    """))),
            @ApiResponse(responseCode = "500", description = "Error interno al listar productos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public List<ProductResponse> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener producto por id",
            description = "Busca un producto por su identificador para consultar su nombre y precio actual.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "No existe un producto con el id informado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "El id informado no es válido",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno al buscar el producto",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ProductResponse findById(
            @Parameter(description = "Identificador del producto", example = "1", required = true)
            @PathVariable @Positive(message = "El id debe ser mayor a 0") Long id) {
        return productService.findById(id);
    }
}
