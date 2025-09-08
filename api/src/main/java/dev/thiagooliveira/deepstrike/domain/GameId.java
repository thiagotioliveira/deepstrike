package dev.thiagooliveira.deepstrike.domain;

import dev.thiagooliveira.deepstrike.domain.exception.DomainException;
import java.util.Objects;
import java.util.UUID;

public record GameId(UUID value) {
  public GameId {
    DomainException.requireNonNull(value);
  }

  public static GameId newId() {
    return new GameId(UUID.randomUUID());
  }

  @Override
  public String toString() {
    return "GameId{" + "value=" + value + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    GameId gameId = (GameId) o;
    return Objects.equals(value, gameId.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
}
