package dev.thiagooliveira.deepstrike.domain;

import dev.thiagooliveira.deepstrike.domain.board.PlayerBoard;
import dev.thiagooliveira.deepstrike.domain.rule.Ruleset;
import java.time.OffsetDateTime;
import java.util.UUID;

public record GameDetail(
    UUID id,
    GameStatus status,
    OffsetDateTime createdAt,
    Ruleset rules,
    PlayerId playerId1,
    PlayerBoard player1Board,
    PlayerId playerId2,
    PlayerBoard player2Board,
    PlayerId currentTurn,
    PlayerId winner) {
  public GameDetail(Game game) {
    this(
        game.getId().value(),
        game.getStatus(),
        game.getCreatedAt(),
        game.getRules(),
        game.getPlayer1(),
        game.getBoards().get(game.getPlayer1()),
        game.getPlayer2(),
        game.getBoards().get(game.getPlayer2()),
        game.getCurrentTurn(),
        game.getWinner());
  }
}
