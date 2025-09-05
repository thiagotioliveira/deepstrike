package com.thiagooliveira.deepstrike.domain.event;

import com.thiagooliveira.deepstrike.domain.GameId;
import com.thiagooliveira.deepstrike.domain.PlayerId;
import java.time.Instant;
import java.util.UUID;

public record GameWon(GameId id, PlayerId winner, Instant occurredAt) implements DomainEvent {

  public GameWon(GameId id, PlayerId playerId) {
    this(id, playerId, Instant.now());
  }

  @Override
  public UUID aggregateId() {
    return id.value();
  }
}
