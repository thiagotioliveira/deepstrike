package dev.thiagooliveira.deepstrike.domain.event;

import dev.thiagooliveira.deepstrike.domain.GameId;
import dev.thiagooliveira.deepstrike.domain.PlayerId;
import java.time.Instant;
import java.util.UUID;

public record GameWon(GameId id, PlayerId winner, Instant occurredAt, int version)
    implements DomainEvent {

  public GameWon(GameId id, PlayerId playerId, int version) {
    this(id, playerId, Instant.now(), version);
  }

  @Override
  public UUID aggregateId() {
    return id.value();
  }
}
