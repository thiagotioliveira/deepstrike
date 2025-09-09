package dev.thiagooliveira.deepstrike.domain.event;

import dev.thiagooliveira.deepstrike.domain.Coordinate;
import dev.thiagooliveira.deepstrike.domain.GameId;
import dev.thiagooliveira.deepstrike.domain.PlayerId;
import dev.thiagooliveira.deepstrike.domain.board.ShotResult;
import java.time.Instant;
import java.util.UUID;

public record ShotResolved(
    GameId id,
    PlayerId playerId,
    Coordinate coordinate,
    ShotResult shotResult,
    Instant occurredAt,
    int version)
    implements DomainEvent {
  public ShotResolved(
      GameId id, PlayerId playerId, Coordinate coordinate, ShotResult shotResult, int version) {
    this(id, playerId, coordinate, shotResult, Instant.now(), version);
  }

  @Override
  public UUID aggregateId() {
    return id.value();
  }
}
