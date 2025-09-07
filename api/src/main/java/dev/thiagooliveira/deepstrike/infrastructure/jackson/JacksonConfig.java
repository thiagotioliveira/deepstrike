package dev.thiagooliveira.deepstrike.infrastructure.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.thiagooliveira.deepstrike.domain.ship.Ship;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

  @Bean
  public ObjectMapper objectMapper() {
    var mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    SimpleModule module = new SimpleModule();
    module.addDeserializer(Ship.class, new ShipDeserializer());
    mapper.registerModule(module);
    return mapper;
  }
}
