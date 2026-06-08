package br.com.fiap.eclipseprotocol.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI petosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Eclipse Protocol API")
                        .version("1.0")
                        .description("API para monitoramento inteligente de plantações agrícolas utilizando sensores IoT, leituras ambientais e geração de alertas.")
                        .contact(
                                new Contact()
                                        .name("Equipe Eclipse Protocol")
                        )
                );
    }
}

