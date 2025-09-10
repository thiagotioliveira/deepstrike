package dev.thiagooliveira.deepstrike.domain.board;

public enum ShotResult {
  HIT,
  MISS,
  SUNK;

  public boolean isHitOrSunk() {
    return (this == HIT || this == SUNK);
  }
}
