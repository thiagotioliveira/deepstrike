package dev.thiagooliveira.deepstrike.infrastructure.persistence.game;

import dev.thiagooliveira.deepstrike.domain.GameStatus;
import feign.Param;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GameSummaryJpaRepository extends JpaRepository<GameSummaryEntity, UUID> {

  @Query(
      "SELECT g FROM GameSummaryEntity g "
          + "WHERE (g.hostPlayer = :playerId OR g.opponentPlayer = :playerId) "
          + "   OR g.status = :openStatus "
          + "ORDER BY g.createdAt DESC")
  List<GameSummaryEntity> findGamesByPlayerOrOpen(
      @Param("playerId") UUID playerId, @Param("openStatus") GameStatus openStatus);
}
