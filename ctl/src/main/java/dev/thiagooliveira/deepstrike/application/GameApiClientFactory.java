package dev.thiagooliveira.deepstrike.application;

import dev.thiagooliveira.deepstrike.infrastructure.client.ApiClient;
import dev.thiagooliveira.deepstrike.infrastructure.client.api.DefaultApi;
import org.springframework.stereotype.Component;

@Component
public class GameApiClientFactory {
  public DefaultApi create(String baseUrl) {
    ApiClient apiClient = new ApiClient();
    apiClient.setBasePath(baseUrl);
    return apiClient.buildClient(DefaultApi.class);
  }
}
