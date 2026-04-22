package com.example.purchasereportingapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI purchaseReportingOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Purchase Reporting API")
                        .description("API base para reportes de compras")
                        .version("v1")
                        .contact(new Contact().name("Equipo Backend"))
                        .license(new License().name("Uso interno")));
    }
}
