package dev.thiagooliveira.deepstrike.infrastructure.persistence.game;

import dev.thiagooliveira.deepstrike.application.port.outbound.GameSummaryRepository;
import dev.thiagooliveira.deepstrike.domain.GameStatus;
import dev.thiagooliveira.deepstrike.domain.GameSummary;
import dev.thiagooliveira.deepstrike.domain.PlayerId;
import java.time.ZoneOffset;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class GameSummaryRepositoryAdapter implements GameSummaryRepository {

  private final GameSummaryJpaRepository repository;

  public GameSummaryRepositoryAdapter(GameSummaryJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<GameSummary> findAll(PlayerId playerId) {
    return repository.findGamesByPlayerOrOpen(playerId.value(), GameStatus.OPEN).stream()
        .map(
            g ->
                new GameSummary(
                    g.getId(),
                    g.getStatus(),
                    g.getCreatedAt().atOffset(ZoneOffset.UTC),
                    g.getVersion()))
        .toList();
  }
}
