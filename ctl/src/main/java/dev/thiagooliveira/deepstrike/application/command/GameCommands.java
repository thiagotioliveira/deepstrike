package dev.thiagooliveira.deepstrike.application.command;

import dev.thiagooliveira.deepstrike.application.GameApiClientFactory;
import dev.thiagooliveira.deepstrike.application.context.PlayerContext;
import dev.thiagooliveira.deepstrike.infrastructure.client.api.DefaultApi;
import dev.thiagooliveira.deepstrike.infrastructure.client.dto.CreateGameRequest;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class GameCommands {

  private final DefaultApi gameApi;
  private final PlayerContext context;

  public GameCommands(GameApiClientFactory factory, PlayerContext context) {
    this.gameApi = factory.create("http://localhost:8080");
    this.context = context;
  }

  @ShellMethod("Create a new game")
  public String createGame() {
    var response = gameApi.createGame(new CreateGameRequest().hostPlayerId(context.getPlayerId()));
    return "Game had created with ID: " + response.getId();
  }
}
