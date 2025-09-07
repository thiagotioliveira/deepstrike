package dev.thiagooliveira.deepstrike.domain.event;

import dev.thiagooliveira.deepstrike.domain.GameId;
import dev.thiagooliveira.deepstrike.domain.PlayerId;
import java.time.Instant;
import java.util.UUID;

public record PlayerJoined(GameId id, PlayerId joinedPlayerId, Instant occurredAt)
    implements DomainEvent {

  public PlayerJoined(GameId id, PlayerId joinedPlayerId) {
    this(id, joinedPlayerId, Instant.now());
  }

  @Override
  public UUID aggregateId() {
    return id.value();
  }
}
