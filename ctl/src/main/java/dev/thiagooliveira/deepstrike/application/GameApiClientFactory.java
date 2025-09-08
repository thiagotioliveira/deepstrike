package dev.thiagooliveira.deepstrike.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.thiagooliveira.deepstrike.application.command.exception.GameFeignErrorDecoder;
import dev.thiagooliveira.deepstrike.infrastructure.client.ApiClient;
import dev.thiagooliveira.deepstrike.infrastructure.client.api.DefaultApi;
import org.springframework.stereotype.Component;

@Component
public class GameApiClientFactory {

  private final ObjectMapper objectMapper;

  public GameApiClientFactory(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public DefaultApi create(String baseUrl) {
    ApiClient apiClient = new ApiClient();
    apiClient.setBasePath(baseUrl);
    apiClient.getFeignBuilder().errorDecoder(new GameFeignErrorDecoder(objectMapper));
    return apiClient.buildClient(DefaultApi.class);
  }
}
