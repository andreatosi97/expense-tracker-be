package com.imatcoding.expensetracker.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SwaggerConfig {

    @Bean
    @Primary
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "JWT";

        Info info = new Info()
                .title("Expense Tracker BE")
                .description("Expense tracker BE built using Spring Boot 4.0.6")
                .version("0.0.1")
                .contact(new Contact().name("I'm A.T. Coding").url("https://github.com/andreatosi97"));

        SecurityScheme securityScheme = new SecurityScheme()
                // Auth scheme that rides on standard HTTP Authorization header
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat(securitySchemeName);

        return new OpenAPI().info(info)
                // Apply the security scheme "securitySchemeName" globally (= to all endpoints)
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // .components(...) is a programmatic builder method used to define
                //      and register globally reusable elements
                // addSecuritySchemes link "securitySchemeName" with actual scheme
                //      (new Components = an empty container)
                .components(new Components().addSecuritySchemes(securitySchemeName, securityScheme));
    }
}
