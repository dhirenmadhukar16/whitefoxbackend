package com.example.whitefox.config.infrastructure;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI whiteFoxOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("WhiteFox Laundry Management API")
                        .version("1.0.0")
                        .description("Complete API documentation for Admin, Store, Customer, Rider, Truck Logistics, HQ Operations, Payments, Invoices and Tracking."));
    }
}