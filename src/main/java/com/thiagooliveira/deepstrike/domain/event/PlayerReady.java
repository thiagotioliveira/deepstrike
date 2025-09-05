package com.thiagooliveira.deepstrike.domain.event;

import com.thiagooliveira.deepstrike.domain.GameId;
import com.thiagooliveira.deepstrike.domain.PlayerId;
import java.time.Instant;
import java.util.UUID;

public record PlayerReady(GameId id, PlayerId playerId, Instant occurredAt) implements DomainEvent {

  public PlayerReady(GameId id, PlayerId playerId) {
    this(id, playerId, Instant.now());
  }

  @Override
  public UUID aggregateId() {
    return id.value();
  }
}
