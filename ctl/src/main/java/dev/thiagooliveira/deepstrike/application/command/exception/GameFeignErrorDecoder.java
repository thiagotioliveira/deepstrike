package dev.thiagooliveira.deepstrike.application.command.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.thiagooliveira.deepstrike.infrastructure.client.dto.Error;
import feign.Response;
import feign.codec.ErrorDecoder;

public class GameFeignErrorDecoder implements ErrorDecoder {

  private final ObjectMapper objectMapper;

  public GameFeignErrorDecoder(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public Exception decode(String s, Response response) {
    try {
      if (response.body() != null) {
        var apiError = objectMapper.readValue(response.body().asInputStream(), Error.class);
        return new GameApiException(apiError.getMessage(), response.status());
      }
    } catch (Exception e) {
      // fallback se não conseguir desserializar o body
    }
    return new GameApiException("Unexpected error", response.status());
  }
}
