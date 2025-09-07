package dev.thiagooliveira.deepstrike.infrastructure.persistence.game;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameSummaryJpaRepository extends JpaRepository<GameSummaryEntity, UUID> {}
