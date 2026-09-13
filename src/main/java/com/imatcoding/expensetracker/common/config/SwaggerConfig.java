package com.imatcoding.expensetracker.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SwaggerConfig {

    @Bean
    @Primary
    public OpenAPI customOpenAPI() {

        Info info = new Info()
                .title("Expense Tracker BE")
                .description("Expense tracker BE built using Spring Boot 4.0.6")
                .version("0.0.1")
                .contact(new Contact().name("I'm A.T. Coding").url("https://github.com/andreatosi97"));

        return new OpenAPI().info(info);
    }
}
