package dev.thiagooliveira.deepstrike.domain;

import dev.thiagooliveira.deepstrike.domain.board.PlayerBoard;
import dev.thiagooliveira.deepstrike.domain.board.ShotResult;
import dev.thiagooliveira.deepstrike.domain.event.*;
import dev.thiagooliveira.deepstrike.domain.exception.DomainException;
import dev.thiagooliveira.deepstrike.domain.rule.Ruleset;
import dev.thiagooliveira.deepstrike.domain.ship.Orientation;
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
  private PlayerId player1;
  private PlayerId player2;
  private PlayerId currentTurn;
  private GameStatus status = GameStatus.OPEN;
  private PlayerId winner;
  private String currentLog;

  private int version = 0;

  private final List<DomainEvent> pendingEvents = new ArrayList<>();

  private Game(GameId id, Ruleset rules) {
    this.id = id;
    this.rules = rules;
  }

  private Game(Ruleset rules) {
    this(GameId.newId(), rules);
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
    game.apply(new GameCreated(game.id, hostPlayer, rules, 1));
    return game;
  }

  public void join(PlayerId playerId) {
    ensureStatus(GameStatus.OPEN);
    if (boards.containsKey(playerId)) {
      throw DomainException.badRequest("player already joined");
    }
    if (boards.size() >= 2) {
      throw DomainException.badRequest("game already full");
    }
    apply(new PlayerJoined(id, playerId, version + 1));
  }

  public void placeFleet(PlayerId playerId, List<Ship> ships) {
    ensureStatus(GameStatus.SETUP);
    if (ships == null || ships.isEmpty()) {
      ships = generateRandom();
    }
    validateFleet(ships);
    apply(new FleetPlaced(id, playerId, ships, version + 1));
  }

  public void markReady(PlayerId playerId) {
    ensureStatus(GameStatus.SETUP);
    if (!boards.containsKey(playerId)) {
      throw DomainException.badRequest("player not in game");
    }
    apply(new PlayerReady(id, playerId, version + 1));
    if (readyPlayers.size() == 2) {
      var firstTurn = chooseFirstTurn();
      apply(new TurnStarted(id, firstTurn, version + 1));
    }
  }

  public ShotResult fireShot(PlayerId playerId, Coordinate target) {
    ensureStatus(GameStatus.IN_PROGRESS);
    if (!Objects.equals(playerId, currentTurn)) {
      throw DomainException.badRequest("not your turn");
    }

    var opponent =
        boards.keySet().stream().filter(p -> !p.equals(playerId)).findFirst().orElseThrow();

    PlayerBoard opponentBoard = boards.get(opponent);
    ShotResult result = opponentBoard.previewShot(target);

    apply(new ShotFired(id, playerId, target, version + 1));
    apply(new ShotResolved(id, playerId, target, result, version + 1));

    if (opponentBoard.allShipsSunk()) {
      apply(new GameWon(id, playerId, version + 1));
    } else {
      apply(new TurnStarted(id, opponent, version + 1));
    }

    return result;
  }

  public void markEventsCommitted() {
    pendingEvents.clear();
  }

  public boolean isPlayerInGame(PlayerId playerId) {
    return boards.containsKey(playerId);
  }

  public GameId getId() {
    return id;
  }

  public PlayerId getPlayer1() {
    return player1;
  }

  public PlayerId getPlayer2() {
    return player2;
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

  public String getCurrentLog() {
    return currentLog;
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

  public Ruleset getRules() {
    return rules;
  }

  public Map<PlayerId, PlayerBoard> getBoards() {
    return Collections.unmodifiableMap(boards);
  }

  private void applyFromHistory(DomainEvent e) {
    when(e);
    version++;
  }

  private void apply(DomainEvent event) {
    when(event);
    pendingEvents.add(event);
    version = event.version();
  }

  private void when(DomainEvent e) {
    switch (e) {
      case GameCreated ev -> {
        createdAt = ev.occurredAt().atOffset(ZoneOffset.UTC);
        boards.put(ev.hostPlayer(), new PlayerBoard(rules.boardSize()));
        player1 = ev.hostPlayer();
        status = GameStatus.OPEN;
        currentLog = ev.hostPlayer().value() + " created game " + id.value();
      }
      case PlayerJoined ev -> {
        boards.put(ev.joinedPlayerId(), new PlayerBoard(rules.boardSize()));
        player2 = ev.joinedPlayerId();
        if (boards.size() == 2) {
          status = GameStatus.SETUP;
        }
        currentLog = ev.joinedPlayerId().value() + " joined game";
      }
      case FleetPlaced ev -> {
        boards.get(ev.playerId()).placeFleet(ev.ships());
        currentLog = ev.playerId().value() + " placed fleet for game";
      }
      case PlayerReady ev -> {
        readyPlayers.add(ev.playerId());
        currentLog = ev.playerId().value() + " marked ready for game";
      }
      case TurnStarted ev -> {
        currentTurn = ev.playerId();
        status = GameStatus.IN_PROGRESS;
        currentLog = ev.playerId().value() + " started turn";
      }
      case ShotFired ev -> {
        currentLog = ev.playerId().value() + " shot in " + ev.coordinate();
      }
      case ShotResolved ev -> {
        var opponent =
            boards.keySet().stream()
                .filter(p -> !p.equals(ev.playerId()))
                .findFirst()
                .orElseThrow();
        PlayerBoard opponentBoard = boards.get(opponent);
        opponentBoard.registerShot(ev.coordinate(), ev.shotResult());
        currentLog =
            ev.playerId().value()
                + (ev.shotResult().isHitOrSunk()
                    ? " was successful in shooting " + ev.coordinate()
                    : " missed the shot " + ev.coordinate());
      }
      case GameWon ev -> {
        winner = ev.winner();
        status = GameStatus.FINISHED;
        currentLog = ev.winner().value() + " won game";
      }
      default -> throw DomainException.badRequest("unhandled event " + e);
    }
  }

    private List<Ship> generateRandom() {
        List<Ship> ships = new ArrayList<>();
        Set<Coordinate> occupied = new HashSet<>(); // track all occupied coordinates
        Random random = new Random();
        int boardSize = rules.boardSize();

        for (Ruleset.ShipSpec spec : rules.fleet()) {
            int length = spec.size();
            boolean placed = false;
            while (!placed) {
                Orientation orientation = random.nextBoolean() ? Orientation.HORIZONTAL : Orientation.VERTICAL;

                int x = random.nextInt(boardSize);
                int y = random.nextInt(boardSize);

                // Adjust coordinates to fit inside the board
                if (orientation == Orientation.HORIZONTAL && x + length > boardSize) {
                    x = boardSize - length;
                }
                if (orientation == Orientation.VERTICAL && y + length > boardSize) {
                    y = boardSize - length;
                }

                Coordinate bow = new Coordinate(x, y);
                Ship candidate = new Ship(spec.type(), bow, orientation);

                // Check for overlap
                boolean overlaps = candidate.getFootprint().stream().anyMatch(occupied::contains);
                if (overlaps) continue; // try a new position

                // Mark all coordinates as occupied
                occupied.addAll(candidate.getFootprint());
                ships.add(candidate);
                placed = true;
            }
        }
        return ships;
    }

    private void ensureStatus(GameStatus expected) {
    if (status != expected) {
      throw DomainException.badRequest("expected status " + expected + " but was " + status);
    }
  }

  private void validateFleet(List<Ship> ships) {
    // valida regras: tamanho, sobreposição, limites...
    // (aqui simplificado) //TODO
    int expected = rules.fleet().stream().mapToInt(Ruleset.ShipSpec::quantity).sum();
    if (ships.size() != expected) {
      throw DomainException.badRequest("fleet size mismatch");
    }
  }

  private PlayerId chooseFirstTurn() {
    return boards.keySet().stream().findAny().orElseThrow();
  }
}
