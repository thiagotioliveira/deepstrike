package dev.thiagooliveira.deepstrike.infrastructure.api.exception;

import org.springframework.http.HttpStatus;

public class GameApiException extends RuntimeException {
  private final HttpStatus status;

  private GameApiException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  public static GameApiException notFound(String message) {
    return new GameApiException(message, HttpStatus.NOT_FOUND);
  }

  public HttpStatus getStatus() {
    return status;
  }
}
