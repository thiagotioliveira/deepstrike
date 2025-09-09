package dev.thiagooliveira.deepstrike.infrastructure.persistence.eventstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.thiagooliveira.deepstrike.application.port.outbound.EventStore;
import dev.thiagooliveira.deepstrike.domain.event.DomainEvent;
import dev.thiagooliveira.deepstrike.domain.exception.DomainException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EventStoreAdapter implements EventStore {

  private static final String PREFIX_EVENT_TYPE = "dev.thiagooliveira.deepstrike.domain.event.";

  private final EventJpaRepository repository;
  private final ObjectMapper objectMapper;

  public EventStoreAdapter(EventJpaRepository repository, ObjectMapper objectMapper) {
    this.repository = repository;
    this.objectMapper = objectMapper;
  }

  @Override
  @Transactional
  public void append(UUID aggregateId, List<DomainEvent> events, int expectedVersion) {
    List<EventEntity> current = repository.findByAggregateIdOrderByVersion(aggregateId);

    if (current.size() != expectedVersion) {
      throw DomainException.conflict("concurrency error: version mismatch");
    }

    int version = expectedVersion;
    for (DomainEvent event : events) {
      try {
        EventEntity entity = new EventEntity();
        entity.setAggregateId(aggregateId);
        entity.setVersion(++version);
        entity.setEventType(event.getClass().getSimpleName());
        entity.setPayload(objectMapper.writeValueAsString(event));
        entity.setOccurredAt(event.occurredAt());

        repository.save(entity);
      } catch (Exception e) {
        throw new RuntimeException("error serializing event", e);
      }
    }
  }

  @Override
  public List<DomainEvent> load(UUID aggregateId) {
    List<EventEntity> entities = repository.findByAggregateIdOrderByVersion(aggregateId);
    return convertToDomainEvents(entities);
  }

  @Override
  public List<DomainEvent> load(UUID aggregateId, int upToVersion) {
    List<EventEntity> entities =
        repository.findByAggregateIdAndVersionLessThanEqualOrderByVersion(aggregateId, upToVersion);
    return convertToDomainEvents(entities);
  }

  private @NotNull List<DomainEvent> convertToDomainEvents(List<EventEntity> entities) {
    List<DomainEvent> events = new ArrayList<>();
    for (EventEntity entity : entities) {
      try {
        Class<?> clazz = Class.forName(PREFIX_EVENT_TYPE.concat(entity.getEventType()));
        DomainEvent event = (DomainEvent) objectMapper.readValue(entity.getPayload(), clazz);
        events.add(event);
      } catch (Exception e) {
        throw new RuntimeException("Error deserializing event", e);
      }
    }
    return events;
  }
}
