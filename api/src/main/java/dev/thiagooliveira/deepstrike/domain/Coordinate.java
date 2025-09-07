package dev.thiagooliveira.deepstrike.domain;

public record Coordinate(int x, int y) {
  public Coordinate {
    if (x < 0 || y < 0) {
      throw new IllegalArgumentException("Coordinates must be >= 0");
    }
  }
}
