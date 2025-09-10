package dev.thiagooliveira.deepstrike.application.command.model;

import dev.thiagooliveira.deepstrike.infrastructure.client.dto.GameDetailResponse;
import dev.thiagooliveira.deepstrike.infrastructure.client.dto.Player;
import java.time.OffsetDateTime;
import java.util.UUID;

public record GameDetailViewModel(
    UUID id,
    OffsetDateTime createdAt,
    String status,
    String currentTurn,
    String winner,
    int boardSize,
    Player player1,
    Player player2,
    String currentLog) {
  public static GameDetailViewModel from(GameDetailResponse dto) {
    return new GameDetailViewModel(
        dto.getId(),
        dto.getCreatedAt(),
        dto.getStatus(),
        dto.getCurrentTurn() != null ? dto.getCurrentTurn() : null,
        dto.getWinner() != null ? dto.getWinner() : null,
        dto.getRules().getBoardSize(),
        dto.getPlayer1(),
        dto.getPlayer2(),
        dto.getCurrentLog());
  }
}
