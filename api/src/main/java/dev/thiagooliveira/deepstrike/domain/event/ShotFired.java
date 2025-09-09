package dev.thiagooliveira.deepstrike.domain.event;

import dev.thiagooliveira.deepstrike.domain.Coordinate;
import dev.thiagooliveira.deepstrike.domain.GameId;
import dev.thiagooliveira.deepstrike.domain.PlayerId;
import java.time.Instant;
import java.util.UUID;

public record ShotFired(
    GameId id, PlayerId playerId, Coordinate coordinate, Instant occurredAt, int version)
    implements DomainEvent {
  public ShotFired(GameId id, PlayerId playerId, Coordinate coordinate, int version) {
    this(id, playerId, coordinate, Instant.now(), version);
  }

  @Override
  public UUID aggregateId() {
    return id.value();
  }
}
