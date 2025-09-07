package dev.thiagooliveira.deepstrike.application.port.outbound;

import dev.thiagooliveira.deepstrike.domain.event.DomainEvent;
import java.util.List;
import java.util.UUID;

public interface EventStore {
  void append(UUID aggregateId, List<DomainEvent> events, int expectedVersion);

  List<DomainEvent> load(UUID aggregateId);
}
