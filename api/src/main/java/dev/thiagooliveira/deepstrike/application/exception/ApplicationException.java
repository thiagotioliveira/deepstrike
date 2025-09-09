package dev.thiagooliveira.deepstrike.application.exception;

public class ApplicationException extends RuntimeException {

  private int statusCode = 400;

  private ApplicationException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

  public static ApplicationException notFound(String message) {
    return new ApplicationException(message, 404);
  }

  public static ApplicationException badRequest(String message) {
    return new ApplicationException(message, 400);
  }

  public int getStatusCode() {
    return statusCode;
  }
}
