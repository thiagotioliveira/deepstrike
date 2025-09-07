package dev.thiagooliveira.deepstrike.application.usecase;

import static dev.thiagooliveira.deepstrike.infrastructure.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;

import dev.thiagooliveira.deepstrike.IntegrationTest;
import dev.thiagooliveira.deepstrike.application.command.MarkReadyCommand;
import dev.thiagooliveira.deepstrike.domain.GameId;
import dev.thiagooliveira.deepstrike.domain.GameStatus;
import dev.thiagooliveira.deepstrike.domain.PlayerId;
import dev.thiagooliveira.deepstrike.infrastructure.persistence.eventstore.EventJpaRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MarkReadyUseCaseIT extends IntegrationTest {

  @Autowired private MarkReadyUseCase useCase;

  @Autowired public EventJpaRepository repository;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Test
  void shouldMarkGameAsReadyForPlayer1() {
    UUID gameId = null;
    for (var event : eventsForFleetPlacedForPlayer1()) {
      gameId = repository.save(event).getAggregateId();
    }
    var command = new MarkReadyCommand(new GameId(gameId), new PlayerId(PLAYER_ID_1));
    var game = useCase.handle(command);
    assertNotNull(game);
    assertEquals(GameStatus.SETUP, game.getStatus());
  }

  @Test
  void shouldMarkGameAsReadyForPlayer1And2() {
    UUID gameId = null;
    for (var event : eventsForFleetPlacedForPlayer2()) {
      gameId = repository.save(event).getAggregateId();
    }
    var command = new MarkReadyCommand(new GameId(gameId), new PlayerId(PLAYER_ID_2));
    var game = useCase.handle(command);
    assertNotNull(game);
    assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
  }
}
