package org.example.hris.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI hrisOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HRIS API Documentation")
                        .description("API untuk Human Resource Information System (HRIS)")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("HRIS Backend Team")
                                .email("dev@hris-example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("HRIS Wiki")
                        .url("https://docs.hris-example.com"));
    }
}
