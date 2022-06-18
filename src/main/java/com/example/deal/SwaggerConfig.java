package com.example.deal;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Deal")
                                .contact(
                                        new Contact()
                                                .email("dmitriev_alexandr93@mail.ru")
                                                .name("Dmitriev Aleksandr")

                                )
                );
    }
}