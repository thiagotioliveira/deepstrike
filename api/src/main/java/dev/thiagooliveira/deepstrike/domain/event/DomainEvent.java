package dev.thiagooliveira.deepstrike.domain.event;

import java.time.Instant;
import java.util.UUID;

/** Base interface for domain events. */
public interface DomainEvent {

  /** Identifier of the aggregate that generated the event. */
  UUID aggregateId();

  /** Timestamp of when the event was generated. */
  Instant occurredAt();

  /** Version of the aggregate at the time of the event */
  int version();
}
