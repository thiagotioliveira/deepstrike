package dev.thiagooliveira.deepstrike.application.usecase;

import static dev.thiagooliveira.deepstrike.infrastructure.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;

import dev.thiagooliveira.deepstrike.IntegrationTest;
import dev.thiagooliveira.deepstrike.application.command.PlaceFleetCommand;
import dev.thiagooliveira.deepstrike.domain.GameId;
import dev.thiagooliveira.deepstrike.domain.PlayerId;
import dev.thiagooliveira.deepstrike.infrastructure.persistence.eventstore.EventJpaRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PlaceFleetUseCaseIT extends IntegrationTest {

  @Autowired private PlaceFleetUseCase useCase;

  @Autowired public EventJpaRepository repository;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Test
  void handle() {
    UUID gameId = null;
    for (var event : eventsForPlayerJoined()) {
      gameId = repository.save(event).getAggregateId();
    }
    var command =
        new PlaceFleetCommand(new GameId(gameId), new PlayerId(PLAYER_ID_1), createFleet());
    var game = useCase.handle(command);
    assertNotNull(game);
  }
}
