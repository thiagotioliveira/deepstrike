package dev.thiagooliveira.deepstrike.infrastructure.persistence.game;

import dev.thiagooliveira.deepstrike.domain.GameStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "game_summary")
public class GameSummaryEntity {
  @Id private UUID id;

  @Enumerated(EnumType.STRING)
  private GameStatus status;

  private UUID hostPlayer;
  private UUID opponentPlayer;
  private UUID currentTurn;
  private UUID winner;
  private Instant createdAt;
  private Instant updatedAt;

  public GameSummaryEntity() {}

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public GameStatus getStatus() {
    return status;
  }

  public void setStatus(GameStatus status) {
    this.status = status;
  }

  public UUID getHostPlayer() {
    return hostPlayer;
  }

  public void setHostPlayer(UUID hostPlayer) {
    this.hostPlayer = hostPlayer;
  }

  public UUID getOpponentPlayer() {
    return opponentPlayer;
  }

  public void setOpponentPlayer(UUID opponentPlayer) {
    this.opponentPlayer = opponentPlayer;
  }

  public UUID getCurrentTurn() {
    return currentTurn;
  }

  public void setCurrentTurn(UUID currentTurn) {
    this.currentTurn = currentTurn;
  }

  public UUID getWinner() {
    return winner;
  }

  public void setWinner(UUID winner) {
    this.winner = winner;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }
}
