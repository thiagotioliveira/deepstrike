package dev.thiagooliveira.deepstrike.domain.exception;

public class DomainException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  private int statusCode = 400;

  private DomainException(int statusCode, String message) {
    super(message);
    this.statusCode = statusCode;
  }

  public static DomainException badRequest(String message) {
    return new DomainException(400, message);
  }

  public static DomainException conflict(String message) {
    return new DomainException(409, message);
  }

  public static <T> T requireNonNull(T obj, String message) {
    if (obj == null) {
      throw DomainException.badRequest(message);
    }
    return obj;
  }

  public static <T> T requireNonNull(T obj) {
    return requireNonNull(obj, "argument cannot be null");
  }

  public int getStatusCode() {
    return statusCode;
  }
}
