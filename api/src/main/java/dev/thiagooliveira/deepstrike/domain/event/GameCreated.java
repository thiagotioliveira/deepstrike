package dev.thiagooliveira.deepstrike.domain.event;

import dev.thiagooliveira.deepstrike.domain.GameId;
import dev.thiagooliveira.deepstrike.domain.PlayerId;
import dev.thiagooliveira.deepstrike.domain.rule.Ruleset;
import java.time.Instant;
import java.util.UUID;

public record GameCreated(
    GameId id, PlayerId hostPlayer, Ruleset rules, Instant occurredAt, int version)
    implements DomainEvent {

  public GameCreated(GameId id, PlayerId hostPlayer, Ruleset rules, int version) {
    this(id, hostPlayer, rules, Instant.now(), version);
  }

  @Override
  public UUID aggregateId() {
    return id.value();
  }
}
