package dev.thiagooliveira.deepstrike.infrastructure.persistence.game;

import dev.thiagooliveira.deepstrike.domain.GameStatus;
import dev.thiagooliveira.deepstrike.domain.event.GameCreated;
import dev.thiagooliveira.deepstrike.domain.event.GameWon;
import dev.thiagooliveira.deepstrike.domain.event.PlayerJoined;
import dev.thiagooliveira.deepstrike.domain.event.TurnStarted;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class GameSummaryProjection {

  private final GameSummaryJpaRepository repository;

  public GameSummaryProjection(GameSummaryJpaRepository repository) {
    this.repository = repository;
  }

  @TransactionalEventListener
  public void onGameCreated(GameCreated event) {
    GameSummaryEntity summary = new GameSummaryEntity();
    summary.setId(event.id().value());
    summary.setStatus(GameStatus.OPEN);
    summary.setHostPlayer(event.hostPlayer().value());
    summary.setCreatedAt(event.occurredAt());
    summary.setUpdatedAt(event.occurredAt());

    repository.save(summary);
  }

  @TransactionalEventListener
  public void onPlayerJoined(PlayerJoined event) {
    repository
        .findById(event.id().value())
        .ifPresent(
            summary -> {
              summary.setOpponentPlayer(event.joinedPlayerId().value());
              summary.setStatus(GameStatus.SETUP);
              summary.setUpdatedAt(event.occurredAt());
              repository.save(summary);
            });
  }

  @TransactionalEventListener
  public void onTurnStarted(TurnStarted event) {
    repository
        .findById(event.id().value())
        .ifPresent(
            summary -> {
              summary.setCurrentTurn(event.playerId().value());
              summary.setStatus(GameStatus.IN_PROGRESS);
              summary.setUpdatedAt(event.occurredAt());
              repository.save(summary);
            });
  }

  @TransactionalEventListener
  public void onGameWon(GameWon event) {
    repository
        .findById(event.id().value())
        .ifPresent(
            summary -> {
              summary.setWinner(event.winner().value());
              summary.setStatus(GameStatus.FINISHED);
              summary.setUpdatedAt(event.occurredAt());
              repository.save(summary);
            });
  }
}
