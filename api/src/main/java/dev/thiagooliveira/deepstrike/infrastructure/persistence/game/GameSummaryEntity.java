package dev.thiagooliveira.deepstrike.infrastructure.persistence.game;

import dev.thiagooliveira.deepstrike.domain.GameStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "game_summary")
public class GameSummaryEntity {
  @Id private UUID id;

  @Column(nullable = false)
  private int version;

  @Enumerated(EnumType.STRING)
  private GameStatus status;

  private String player1;
  private String player2;
  private String currentTurn;
  private String winner;
  private Instant createdAt;
  private Instant updatedAt;

  public GameSummaryEntity() {}

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
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

  public String getPlayer1() {
    return player1;
  }

  public void setPlayer1(String player1) {
    this.player1 = player1;
  }

  public String getPlayer2() {
    return player2;
  }

  public void setPlayer2(String player2) {
    this.player2 = player2;
  }

  public String getCurrentTurn() {
    return currentTurn;
  }

  public void setCurrentTurn(String currentTurn) {
    this.currentTurn = currentTurn;
  }

  public String getWinner() {
    return winner;
  }

  public void setWinner(String winner) {
    this.winner = winner;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }
}
