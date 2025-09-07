package dev.thiagooliveira.deepstrike.domain;

import static dev.thiagooliveira.deepstrike.infrastructure.TestUtil.createFleet;
import static org.junit.jupiter.api.Assertions.*;

import dev.thiagooliveira.deepstrike.domain.event.GameCreated;
import dev.thiagooliveira.deepstrike.domain.event.PlayerJoined;
import dev.thiagooliveira.deepstrike.domain.rule.Ruleset;
import dev.thiagooliveira.deepstrike.domain.ship.Ship;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;

class GameTest {

  private PlayerId player1;
  private PlayerId player2;
  private Ruleset rules;

  @BeforeEach
  void setUp() {
    player1 = PlayerId.newId();
    player2 = PlayerId.newId();
    rules = Ruleset.formation2002();
  }

  @Test
  void shouldCreateGameWithHostPlayer() {
    Game game = Game.create(player1, rules);

    assertNotNull(game.getId());
    assertNotNull(game.getCreatedAt());
    assertEquals(GameStatus.OPEN, game.getStatus());
    assertEquals(1, game.getPendingEvents().size());
    assertTrue(game.getPendingEvents().get(0) instanceof GameCreated);
    assertEquals(
        game.getCreatedAt().toInstant(),
        ((GameCreated) game.getPendingEvents().get(0)).occurredAt());
  }

  @Test
  void shouldAllowSecondPlayerToJoin() {
    Game game = Game.create(player1, rules);
    game.markEventsCommitted();

    game.join(player2);

    assertEquals(GameStatus.SETUP, game.getStatus());
    assertEquals(1, game.getPendingEvents().size());
    assertTrue(game.getPendingEvents().get(0) instanceof PlayerJoined);
  }

  @Test
  void shouldPlaceFleetAndMarkReady() {
    Game game = Game.create(player1, rules);
    game.join(player2);
    game.markEventsCommitted();

    List<Ship> fleet = createFleet();

    game.placeFleet(player1, fleet);
    game.placeFleet(player2, fleet);

    game.markReady(player1);
    game.markReady(player2);

    assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
    assertNotNull(game.getCurrentTurn());
  }

  @Test
  void shouldFireShotAndEventuallyWin() {
    Game game = Game.create(player1, rules);
    game.join(player2);
    List<Ship> fleet = createFleet();

    game.placeFleet(player1, fleet);
    game.placeFleet(player2, fleet);
    game.markReady(player1);
    game.markReady(player2);

    PlayerId shooter = game.getCurrentTurn();
    PlayerId opponent = shooter.equals(player1) ? player2 : player1;

    // Fire at all coordinates of opponent's ships
    List<Coordinate> targets = new ArrayList<>();
    for (Ship ship : fleet) {
      targets.addAll(ship.getFootprint()); // ensures we hit all parts of the ship
    }

    for (Coordinate target : targets) {
      game.fireShot(shooter, target);

      // if game is not finished, next turn goes to the same player
      if (game.getStatus() != GameStatus.FINISHED) {
        // alternate turns (game always changes to opponent)
        shooter = game.getCurrentTurn();
        opponent = shooter.equals(player1) ? player2 : player1;
      }
    }

    assertEquals(GameStatus.FINISHED, game.getStatus());
    assertEquals(shooter, game.getWinner());
  }
}
