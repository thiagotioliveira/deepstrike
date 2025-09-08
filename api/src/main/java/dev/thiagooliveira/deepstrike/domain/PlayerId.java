package dev.thiagooliveira.deepstrike.domain;

import dev.thiagooliveira.deepstrike.domain.exception.DomainException;
import java.util.Objects;

public record PlayerId(String value) {
  public PlayerId {
    DomainException.requireNonNull(value);
    value = value.trim();
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
