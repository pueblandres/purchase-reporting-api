package com.example.purchasereportingapi.exception;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

class GlobalExceptionHandlerMockMvcTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(new TestExceptionController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .setValidator(validator)
                .build();
    }

    @Test
    void shouldReturnBadRequestForMethodArgumentNotValidException() throws Exception {
        mockMvc.perform(post("/test-exceptions/validation")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "name": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.message").value("Hay errores de validación en la solicitud"))
                .andExpect(jsonPath("$.path").value("/test-exceptions/validation"))
                .andExpect(jsonPath("$.validations[0].field").value("name"))
                .andExpect(jsonPath("$.validations[0].message").value("El nombre es obligatorio"));
    }

    @Test
    void shouldReturnBadRequestForHttpMessageNotReadableException() throws Exception {
        mockMvc.perform(post("/test-exceptions/json")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "date": "fecha-invalida"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("El cuerpo de la solicitud no es válido o tiene un formato JSON incorrecto"))
                .andExpect(jsonPath("$.path").value("/test-exceptions/json"));
    }

    @Test
    void shouldReturnNotFoundForResourceNotFoundException() throws Exception {
        mockMvc.perform(get("/test-exceptions/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Recurso de prueba no encontrado"))
                .andExpect(jsonPath("$.path").value("/test-exceptions/not-found"));
    }

    @Test
    void shouldReturnInternalServerErrorForGenericException() throws Exception {
        mockMvc.perform(get("/test-exceptions/generic"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Error inesperado"))
                .andExpect(jsonPath("$.path").value("/test-exceptions/generic"));
    }

    @RestController
    @RequestMapping("/test-exceptions")
    static class TestExceptionController {

        @PostMapping("/validation")
        String validate(@Valid @RequestBody ValidationRequest request) {
            return "ok";
        }

        @PostMapping("/json")
        String parseJson(@RequestBody DateRequest request) {
            return "ok";
        }

        @GetMapping("/not-found")
        String notFound() {
            throw new ResourceNotFoundException("Recurso de prueba no encontrado");
        }

        @GetMapping("/generic")
        String generic() {
            throw new RuntimeException("boom");
        }
    }

    static class ValidationRequest {
        @NotBlank(message = "El nombre es obligatorio")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class DateRequest {
        private LocalDateTime date;

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }
    }
}
