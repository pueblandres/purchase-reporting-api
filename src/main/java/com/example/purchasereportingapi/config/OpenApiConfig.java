package com.example.purchasereportingapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI purchaseApiDocs() {
        return new OpenAPI()
                .info(new Info()
                        .title("Purchase Reporting API")
                        .version("v1")
                        .description("API REST para usuarios, productos, compras y reportes")
                        .contact(new Contact().name("Backend Team").email("backend@example.com")));
    }
}
