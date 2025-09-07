package dev.thiagooliveira.deepstrike.domain;

import dev.thiagooliveira.deepstrike.domain.board.PlayerBoard;
import dev.thiagooliveira.deepstrike.domain.board.ShotResult;
import dev.thiagooliveira.deepstrike.domain.event.*;
import dev.thiagooliveira.deepstrike.domain.rule.Ruleset;
import dev.thiagooliveira.deepstrike.domain.ship.Ship;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class Game {

  private final GameId id;
  private OffsetDateTime createdAt;
  private final Ruleset rules;
  private final Map<PlayerId, PlayerBoard> boards = new HashMap<>();
  private final Set<PlayerId> readyPlayers = new HashSet<>();
  private PlayerId currentTurn;
  private GameStatus status = GameStatus.OPEN;
  private PlayerId winner;

  private int version = 0;

  private final List<DomainEvent> pendingEvents = new ArrayList<>();

  private Game(GameId id, Ruleset rules) {
    this.id = id;
    this.rules = rules;
  }

  private Game(Ruleset rules) {
    this.id = GameId.newId();
    this.rules = rules;
  }

  public static Game rehydrate(List<DomainEvent> events) {
    Game game = null;
    for (DomainEvent event : events) {
      if (event instanceof GameCreated gc) {
        game = new Game(new GameId(gc.aggregateId()), gc.rules());
        game.applyFromHistory(gc);
      } else if (game != null) {
        game.applyFromHistory(event);
      }
    }
    return game;
  }

  public static Game create(PlayerId hostPlayer, Ruleset rules) {
    Game game = new Game(rules);
    game.apply(new GameCreated(game.id, hostPlayer, rules));
    return game;
  }

  public void join(PlayerId playerId) {
    ensureStatus(GameStatus.OPEN);
    if (boards.containsKey(playerId)) {
      throw new IllegalStateException("Player already joined");
    }
    if (boards.size() >= 2) {
      throw new IllegalStateException("Game already full");
    }
    apply(new PlayerJoined(id, playerId));
  }

  public void placeFleet(PlayerId playerId, List<Ship> ships) {
    ensureStatus(GameStatus.SETUP);
    validateFleet(ships);
    apply(new FleetPlaced(id, playerId, ships));
  }

  public void markReady(PlayerId playerId) {
    ensureStatus(GameStatus.SETUP);
    if (!boards.containsKey(playerId)) {
      throw new IllegalStateException("Player not in game");
    }
    apply(new PlayerReady(id, playerId));
    if (readyPlayers.size() == 2) {
      var firstTurn = chooseFirstTurn();
      apply(new TurnStarted(id, firstTurn));
    }
  }

  public void fireShot(PlayerId playerId, Coordinate target) {
    ensureStatus(GameStatus.IN_PROGRESS);
    if (!Objects.equals(playerId, currentTurn)) {
      throw new IllegalStateException("Not your turn");
    }

    var opponent =
        boards.keySet().stream().filter(p -> !p.equals(playerId)).findFirst().orElseThrow();

    PlayerBoard opponentBoard = boards.get(opponent);

    ShotResult result = opponentBoard.registerShot(target);

    apply(new ShotFired(id, playerId, target));
    apply(new ShotResolved(id, playerId, target, result));

    if (opponentBoard.allShipsSunk()) {
      apply(new GameWon(id, playerId));
    } else {
      apply(new TurnStarted(id, opponent));
    }
  }

  public void markEventsCommitted() {
    pendingEvents.clear();
  }

  public GameId getId() {
    return id;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public GameStatus getStatus() {
    return status;
  }

  public PlayerId getCurrentTurn() {
    return currentTurn;
  }

  public PlayerId getWinner() {
    return winner;
  }

  public List<DomainEvent> getPendingEvents() {
    return List.copyOf(pendingEvents);
  }

  public int getVersion() {
    return version;
  }

  private void applyFromHistory(DomainEvent e) {
    when(e);
    version++;
  }

  private void apply(DomainEvent event) {
    when(event);
    pendingEvents.add(event);
    version++;
  }

  private void when(DomainEvent e) {
    switch (e) {
      case GameCreated ev -> {
        createdAt = ev.occurredAt().atOffset(ZoneOffset.UTC);
        boards.put(ev.hostPlayer(), new PlayerBoard(rules.boardSize()));
        status = GameStatus.OPEN;
      }
      case PlayerJoined ev -> {
        boards.put(ev.joinedPlayerId(), new PlayerBoard(rules.boardSize()));
        if (boards.size() == 2) {
          status = GameStatus.SETUP;
        }
      }
      case FleetPlaced ev -> {
        boards.get(ev.playerId()).placeFleet(ev.ships());
      }
      case PlayerReady ev -> {
        readyPlayers.add(ev.playerId());
      }
      case TurnStarted ev -> {
        currentTurn = ev.playerId();
        status = GameStatus.IN_PROGRESS;
      }
      case ShotFired ev -> {
        // apenas log, efeito real está em ShotResolved
      }
      case ShotResolved ev -> {
        // atualizar estado do board já feito no PlayerBoard
      }
      case GameWon ev -> {
        winner = ev.winner();
        status = GameStatus.FINISHED;
      }
      default -> throw new IllegalStateException("Unhandled event " + e);
    }
  }

  private void ensureStatus(GameStatus expected) {
    if (status != expected) {
      throw new IllegalStateException("Expected status " + expected + " but was " + status);
    }
  }

  private void validateFleet(List<Ship> ships) {
    // valida regras: tamanho, sobreposição, limites...
    // (aqui simplificado) //TODO
    int expected = rules.fleet().stream().mapToInt(Ruleset.ShipSpec::quantity).sum();
    if (ships.size() != expected) {
      throw new IllegalArgumentException("Fleet size mismatch");
    }
  }

  private PlayerId chooseFirstTurn() {
    return boards.keySet().stream().findAny().orElseThrow();
  }
}
