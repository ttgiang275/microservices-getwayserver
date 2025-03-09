package com.river.cards;

import com.river.cards.dto.CardsContactInfoDto;
import com.river.cards.dto.RiverDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableConfigurationProperties(value = {CardsContactInfoDto.class, RiverDto.class})
@OpenAPIDefinition(
        info = @Info(
                title = "Cards microservice REST API Documentation",
                description = "Cards microservice REST API Documentation",
                version = "v1",
                contact = @Contact(
                        name = "River",
                        email = "ttgiang275@gmail.com",
                        url = "https://www.river.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.river.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Cards microservice REST API Documentation",
                url = "https://www.river.com/swagger-ui.html"
        )
)
public class CardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardsApplication.class, args);
    }

}
