package com.thiagooliveira.deepstrike.domain.event;

import com.thiagooliveira.deepstrike.domain.GameId;
import com.thiagooliveira.deepstrike.domain.PlayerId;
import com.thiagooliveira.deepstrike.domain.rule.Ruleset;
import java.time.Instant;
import java.util.UUID;

public record GameCreated(GameId id, PlayerId hostPlayer, Ruleset rules, Instant occurredAt)
    implements DomainEvent {

  public GameCreated(GameId id, PlayerId hostPlayer, Ruleset rules) {
    this(id, hostPlayer, rules, Instant.now());
  }

  @Override
  public UUID aggregateId() {
    return id.value();
  }
}
