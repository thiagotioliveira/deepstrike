package dev.thiagooliveira.deepstrike.infrastructure.persistence.eventstore;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventJpaRepository extends JpaRepository<EventEntity, Long> {
  List<EventEntity> findByAggregateIdOrderByVersion(UUID aggregateId);
}
