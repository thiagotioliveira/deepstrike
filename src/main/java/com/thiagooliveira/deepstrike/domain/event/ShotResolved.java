package com.thiagooliveira.deepstrike.domain.event;

import com.thiagooliveira.deepstrike.domain.Coordinate;
import com.thiagooliveira.deepstrike.domain.GameId;
import com.thiagooliveira.deepstrike.domain.PlayerId;
import com.thiagooliveira.deepstrike.domain.board.ShotResult;
import java.time.Instant;
import java.util.UUID;

public record ShotResolved(
    GameId id, PlayerId playerId, Coordinate coordinate, ShotResult shotResult, Instant occurredAt)
    implements DomainEvent {
  public ShotResolved(GameId id, PlayerId playerId, Coordinate coordinate, ShotResult shotResult) {
    this(id, playerId, coordinate, shotResult, Instant.now());
  }

  @Override
  public UUID aggregateId() {
    return id.value();
  }
}
