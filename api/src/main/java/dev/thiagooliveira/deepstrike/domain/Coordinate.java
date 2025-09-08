package dev.thiagooliveira.deepstrike.domain;

import dev.thiagooliveira.deepstrike.domain.exception.DomainException;

public record Coordinate(int x, int y) {
  public Coordinate {
    if (x < 0 || y < 0) {
      throw DomainException.badRequest("coordinates must be >= 0");
    }
  }
}
