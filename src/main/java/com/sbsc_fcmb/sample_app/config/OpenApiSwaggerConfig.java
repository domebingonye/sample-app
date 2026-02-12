package com.sbsc_fcmb.sample_app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class OpenApiSwaggerConfig {

    @Bean
    public OpenAPI openApiConfiguration() {
        return new OpenAPI()
                .info(new Info()
                        .title("Dominic API")
                        .version("1.0")
                        .description("SBSC_FCMB")
                        .termsOfService("Terms of service")
                        .license(getLicense())
                        .contact(getContact())
                );
    }

    private Contact getContact() {
        Contact contact = new Contact();
        contact.setEmail("domebngonye@gmail.com");
        contact.setName("SBSC_FCMB");
        contact.setExtensions(Collections.emptyMap());
        return contact;
    }

    private License getLicense() {
        License license = new License();
        license.setName("Apache License, Version 2.0");
        license.setUrl("http://www.apache.org/licenses/LICENSE-2.0");
        license.setExtensions(Collections.emptyMap());
        return license;
    }
}
