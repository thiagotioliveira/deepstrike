package dev.thiagooliveira.deepstrike.infrastructure.persistence.game;

import dev.thiagooliveira.deepstrike.domain.GameStatus;
import dev.thiagooliveira.deepstrike.domain.event.*;
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
    summary.setPlayer1(event.hostPlayer().value());
    summary.setCreatedAt(event.occurredAt());
    summary.setUpdatedAt(event.occurredAt());
    summary.setVersion(event.version());
    repository.save(summary);
  }

  @TransactionalEventListener
  public void onPlayerJoined(PlayerJoined event) {
    repository
        .findById(event.id().value())
        .ifPresent(
            summary -> {
              summary.setPlayer2(event.joinedPlayerId().value());
              summary.setStatus(GameStatus.SETUP);
              summary.setUpdatedAt(event.occurredAt());
              summary.setVersion(event.version());
              repository.save(summary);
            });
  }

  @TransactionalEventListener
  public void onFleetPlaced(FleetPlaced event) {
    repository
        .findById(event.id().value())
        .ifPresent(
            summary -> {
              summary.setUpdatedAt(event.occurredAt());
              summary.setVersion(event.version());
              repository.save(summary);
            });
  }

  @TransactionalEventListener
  public void onPlayerReady(PlayerReady event) {
    repository
        .findById(event.id().value())
        .ifPresent(
            summary -> {
              summary.setUpdatedAt(event.occurredAt());
              summary.setVersion(event.version());
              repository.save(summary);
            });
  }

  @TransactionalEventListener
  public void onShotFired(ShotFired event) {
    repository
        .findById(event.id().value())
        .ifPresent(
            summary -> {
              summary.setUpdatedAt(event.occurredAt());
              summary.setVersion(event.version());
              repository.save(summary);
            });
  }

  @TransactionalEventListener
  public void onShotResolved(ShotResolved event) {
    repository
        .findById(event.id().value())
        .ifPresent(
            summary -> {
              summary.setUpdatedAt(event.occurredAt());
              summary.setVersion(event.version());
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
              summary.setVersion(event.version());
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
              summary.setVersion(event.version());
              repository.save(summary);
            });
  }
}
