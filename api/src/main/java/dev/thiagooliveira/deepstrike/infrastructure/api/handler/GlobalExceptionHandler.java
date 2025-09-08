package dev.thiagooliveira.deepstrike.infrastructure.api.handler;

import dev.thiagooliveira.deepstrike.domain.exception.DomainException;
import dev.thiagooliveira.deepstrike.infrastructure.api.dto.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<Error> handleDomainException(DomainException exception) {
    int statusCode = exception.getStatusCode();
    return ResponseEntity.status(statusCode)
        .body(new Error().code(statusCode).message(exception.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Error> handleException(Exception exception) {
    logger.error("Unexpected error", exception);
    return ResponseEntity.status(500).body(new Error().code(500).message("Unexpected error"));
  }
}
