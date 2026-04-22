package com.example.purchasereportingapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String API_TITLE = "Purchase Reporting API";
    private static final String API_DESCRIPTION = "API de reportes de compras";
    private static final String API_VERSION = "v1";

    @Bean
    public OpenAPI purchaseReportingOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title(API_TITLE)
                        .description(API_DESCRIPTION)
                        .version(API_VERSION)
                        .contact(new Contact()
                                .name("Equipo Backend")
                                .email("backend@company.local"))
                        .license(new License().name("Uso interno")));
    }
}
