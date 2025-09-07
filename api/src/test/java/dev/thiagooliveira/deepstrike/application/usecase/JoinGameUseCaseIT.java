package dev.thiagooliveira.deepstrike.application.usecase;

import static dev.thiagooliveira.deepstrike.infrastructure.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;

import dev.thiagooliveira.deepstrike.IntegrationTest;
import dev.thiagooliveira.deepstrike.application.command.JoinGameCommand;
import dev.thiagooliveira.deepstrike.domain.GameId;
import dev.thiagooliveira.deepstrike.domain.GameStatus;
import dev.thiagooliveira.deepstrike.domain.PlayerId;
import dev.thiagooliveira.deepstrike.infrastructure.eventstore.EventJpaRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class JoinGameUseCaseIT extends IntegrationTest {

  @Autowired public JoinGameUseCase useCase;

  @Autowired public EventJpaRepository repository;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Test
  void handle() {
    UUID gameId = null;
    for (var event : eventsForCreateGame()) {
      gameId = repository.save(event).getAggregateId();
    }
    var playerId = PlayerId.newId();
    var command = new JoinGameCommand(new GameId(gameId), playerId);
    var game = useCase.handle(command);
    assertNotNull(game);
    assertEquals(GameStatus.SETUP, game.getStatus());
  }
}
