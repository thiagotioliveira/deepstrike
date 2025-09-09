package dev.thiagooliveira.deepstrike.domain.event;

import dev.thiagooliveira.deepstrike.domain.GameId;
import dev.thiagooliveira.deepstrike.domain.PlayerId;
import java.time.Instant;
import java.util.UUID;

public record TurnStarted(GameId id, PlayerId playerId, Instant occurredAt, int version)
    implements DomainEvent {

  public TurnStarted(GameId id, PlayerId playerId, int version) {
    this(id, playerId, Instant.now(), version);
  }

  @Override
  public UUID aggregateId() {
    return id.value();
  }
}
