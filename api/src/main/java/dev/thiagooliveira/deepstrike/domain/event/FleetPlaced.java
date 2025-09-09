package dev.thiagooliveira.deepstrike.domain.event;

import dev.thiagooliveira.deepstrike.domain.GameId;
import dev.thiagooliveira.deepstrike.domain.PlayerId;
import dev.thiagooliveira.deepstrike.domain.ship.Ship;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record FleetPlaced(
    GameId id, PlayerId playerId, List<Ship> ships, Instant occurredAt, int version)
    implements DomainEvent {

  public FleetPlaced(GameId id, PlayerId playerId, List<Ship> ships, int version) {
    this(id, playerId, ships, Instant.now(), version);
  }

  @Override
  public UUID aggregateId() {
    return id.value();
  }
}
