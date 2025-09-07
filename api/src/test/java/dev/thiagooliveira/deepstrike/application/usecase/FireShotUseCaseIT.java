package dev.thiagooliveira.deepstrike.application.usecase;

import static dev.thiagooliveira.deepstrike.infrastructure.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;

import dev.thiagooliveira.deepstrike.IntegrationTest;
import dev.thiagooliveira.deepstrike.application.command.FireShotCommand;
import dev.thiagooliveira.deepstrike.domain.Coordinate;
import dev.thiagooliveira.deepstrike.domain.GameId;
import dev.thiagooliveira.deepstrike.domain.PlayerId;
import dev.thiagooliveira.deepstrike.infrastructure.persistence.eventstore.EventJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FireShotUseCaseIT extends IntegrationTest {

  @Autowired private FireShotUseCase useCase;

  @Autowired private EventJpaRepository repository;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Test
  void handle() {
    repository.saveAll(eventsForTurnStarted());
    var command =
        new FireShotCommand(new GameId(GAME_ID), new PlayerId(PLAYER_ID_1), new Coordinate(0, 0));
    var game = useCase.handle(command);
    assertNotNull(game);
  }
}
