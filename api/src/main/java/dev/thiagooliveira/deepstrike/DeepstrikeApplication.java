package dev.thiagooliveira.deepstrike;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DeepstrikeApplication {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        // .components(new Components().addSecuritySchemes("bearerAuth", createAPIKeyScheme()))
        .info(
            new Info()
                .title("DeepStrike - Battleship API")
                .description("DeepStrike - Battleship API")
                .version("1.0.0")
                .contact(
                    new Contact()
                        .name("Thiago Oliveira")
                        .email("thiago@thiagoti.com")
                        .url("http://thiagooliveira.dev")));
  }

  private SecurityScheme createAPIKeyScheme() {
    return new SecurityScheme().type(SecurityScheme.Type.HTTP).bearerFormat("JWT").scheme("bearer");
  }

  public static void main(String[] args) {
    SpringApplication.run(DeepstrikeApplication.class, args);
  }
}
