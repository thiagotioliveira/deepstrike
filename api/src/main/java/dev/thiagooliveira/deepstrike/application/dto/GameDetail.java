package dev.thiagooliveira.deepstrike.application.dto;

import dev.thiagooliveira.deepstrike.domain.Coordinate;
import dev.thiagooliveira.deepstrike.domain.Game;
import dev.thiagooliveira.deepstrike.domain.GameStatus;
import dev.thiagooliveira.deepstrike.domain.PlayerId;
import dev.thiagooliveira.deepstrike.domain.board.PlayerBoard;
import dev.thiagooliveira.deepstrike.domain.ship.ShipType;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record GameDetail(
    UUID id,
    GameStatus status,
    OffsetDateTime createdAt,
    Ruleset rules,
    Player player1,
    Player player2,
    PlayerId currentTurn,
    PlayerId winner) {

  public GameDetail(Game game, PlayerId currentPlayerId) {
    this(
        game.getId().value(),
        game.getStatus(),
        game.getCreatedAt(),
        map(game.getRules()),
        new Player(
            game.getPlayer1(),
            currentPlayerId.equals(game.getPlayer1())
                ? map(game.getBoards().get(game.getPlayer1()))
                : mapWithoutFootprint(game.getBoards().get(game.getPlayer1()))),
        new Player(
            game.getPlayer2(),
            currentPlayerId.equals(game.getPlayer2())
                ? map(game.getBoards().get(game.getPlayer2()))
                : mapWithoutFootprint(game.getBoards().get(game.getPlayer2()))),
        game.getCurrentTurn(),
        game.getWinner());
  }

  private static Board mapWithoutFootprint(PlayerBoard playerBoard) {
    return playerBoard == null
        ? null
        : new Board(
            playerBoard.getShips().stream().map(GameDetail::mapWithoutFootprint).toList(),
            Collections.unmodifiableSet(playerBoard.getShotsReceived()));
  }

  private static Ruleset map(dev.thiagooliveira.deepstrike.domain.rule.Ruleset ruleset) {
    return new Ruleset(ruleset.boardSize(), ruleset.fleet().stream().map(GameDetail::map).toList());
  }

  private static ShipSpec map(dev.thiagooliveira.deepstrike.domain.rule.Ruleset.ShipSpec shipSpec) {
    return new ShipSpec(shipSpec.type(), shipSpec.size(), shipSpec.quantity());
  }

  private static Ship map(dev.thiagooliveira.deepstrike.domain.ship.Ship ship) {
    return new Ship(
        ship.getType().displayName(),
        Collections.unmodifiableList(ship.getFootprint()),
        Collections.unmodifiableSet(ship.getHits()));
  }

  private static Ship mapWithoutFootprint(dev.thiagooliveira.deepstrike.domain.ship.Ship ship) {
    return new Ship(
        ship.getType().displayName(),
        Collections.emptyList(),
        Collections.unmodifiableSet(ship.getHits()));
  }

  public static Board map(PlayerBoard playerBoard) {
    return new Board(
        playerBoard.getShips().stream().map(GameDetail::map).toList(),
        Collections.unmodifiableSet(playerBoard.getShotsReceived()));
  }

  public record Ruleset(int boardSize, List<ShipSpec> fleet) {}

  public record ShipSpec(ShipType type, int size, int quantity) {}

  public record Player(PlayerId id, Board board) {}

  public record Board(List<Ship> ships, Set<Coordinate> shotsReceived) {}

  public record Ship(String type, List<Coordinate> footprint, Set<Coordinate> hits) {}
}
