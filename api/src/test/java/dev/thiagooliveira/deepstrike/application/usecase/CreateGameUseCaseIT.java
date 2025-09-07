package dev.thiagooliveira.deepstrike.application.usecase;

import static org.junit.jupiter.api.Assertions.*;

import dev.thiagooliveira.deepstrike.IntegrationTest;
import dev.thiagooliveira.deepstrike.application.command.CreateGameCommand;
import dev.thiagooliveira.deepstrike.domain.GameStatus;
import dev.thiagooliveira.deepstrike.domain.PlayerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CreateGameUseCaseIT extends IntegrationTest {

  @Autowired private CreateGameUseCase useCase;

  @BeforeEach
  void setUp() {}

  @Test
  void handle() {
    var command = new CreateGameCommand(PlayerId.newId());
    var game = useCase.handle(command);
    assertNotNull(game);
    assertNotNull(game.getId());
    assertNotNull(game.getId().value());
    assertNotNull(game.getCreatedAt());
    assertEquals(GameStatus.OPEN, game.getStatus());
  }
}
