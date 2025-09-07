package dev.thiagooliveira.deepstrike.domain;

import java.util.Objects;
import java.util.UUID;

public record PlayerId(UUID value) {
  public PlayerId {
    Objects.requireNonNull(value);
  }

  public static PlayerId newId() {
    return new PlayerId(UUID.randomUUID());
  }

  @Override
  public String toString() {
    return "PlayerId{" + "value=" + value + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    PlayerId gameId = (PlayerId) o;
    return Objects.equals(value, gameId.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
}
