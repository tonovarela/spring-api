package com.gestion.eventos.api.security.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  private  static final String SCHEME_NAME = "bearerAuth";
  private  static final String BEARER_FORMAT= "JWT";
  private  static final String DESCRIPTION  ="JWT Authentication with Bearer token";
    @Bean
    public OpenAPI customOpenAPI() {

        final String schemeName = "bearerAuth";

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
                .components(new Components().addSecuritySchemes(SCHEME_NAME,
                        new SecurityScheme()
                                .name(SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat(BEARER_FORMAT)
                                .in(SecurityScheme.In.HEADER)
                                .description(DESCRIPTION)
                ))
                .info(new Info().title("API de Gestión de Eventos")
                        .version("1.0.0")
                        .description("API para la gestión de eventos, incluyendo creación, actualización y eliminación de eventos.")
                        .contact(new Contact()
                                .name("Marco Antonio Estelles")
                                .email("tonovarela@live.com")
                                .url("https://tonovarela.com")
                        )
                        .license(new License().name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0"))
                )
                .addSecurityItem(new SecurityRequirement().addList(schemeName))
                .components(new Components().addSecuritySchemes(schemeName,
                        new SecurityScheme()
                                .name(schemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                ));

    }


}
