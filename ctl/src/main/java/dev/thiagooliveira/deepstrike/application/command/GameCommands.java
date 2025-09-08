package dev.thiagooliveira.deepstrike.application.command;

import dev.thiagooliveira.deepstrike.application.GameApiClientFactory;
import dev.thiagooliveira.deepstrike.application.command.exception.GameApiException;
import dev.thiagooliveira.deepstrike.application.command.model.*;
import dev.thiagooliveira.deepstrike.application.command.view.ViewResolver;
import dev.thiagooliveira.deepstrike.application.config.AppContext;
import dev.thiagooliveira.deepstrike.application.service.FleetGenerator;
import dev.thiagooliveira.deepstrike.infrastructure.client.api.DefaultApi;
import dev.thiagooliveira.deepstrike.infrastructure.client.dto.*;
import java.util.UUID;
import java.util.function.Supplier;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class GameCommands {

  private final DefaultApi gameApi;
  private final AppContext context;
  private final FleetGenerator fleetGenerator;
  private final ViewResolver viewResolver;

  public GameCommands(
      GameApiClientFactory factory,
      AppContext context,
      FleetGenerator fleetGenerator,
      ViewResolver viewResolver) {
    this.viewResolver = viewResolver;
    this.gameApi = factory.create(context.getBaseUrl());
    this.context = context;
    this.fleetGenerator = fleetGenerator;
  }

  @ShellMethod("Get context")
  public String context() {
    return viewResolver
        .contextView()
        .render(new ContextViewModel(context.getBaseUrl(), context.getPlayerId()));
  }

  @ShellMethod("Create a new game")
  public String create() {
    return executeCommand(
        () ->
            viewResolver
                .createGameView()
                .render(
                    new CreateGameViewModel(
                        gameApi
                            .createGame(new CreateGameRequest().hostPlayerId(context.getPlayerId()))
                            .getId())));
  }

  @ShellMethod("Get detail of a game by id")
  public String detail(String gameId) {
    return executeCommand(
        () ->
            viewResolver
                .gameDetailView()
                .render(GameDetailViewModel.from(gameApi.detail(UUID.fromString(gameId)))));
  }

  @ShellMethod("List games")
  public String list() {
    return executeCommand(
        () ->
            viewResolver
                .listView()
                .render(new ListGamesViewModel(gameApi.listGames(context.getPlayerId()))));
  }

  @ShellMethod("Join an existing game")
  public String join(String gameId) {
    return executeCommand(
        () -> {
          UUID id = UUID.fromString(gameId);
          gameApi.joinGame(id, new JoinGameRequest().playerId(context.getPlayerId()));
          return viewResolver.joinGameView().render(new JoinGameViewModel(id));
        });
  }

  @ShellMethod("Mark player as ready")
  public String ready(String gameId) {
    return executeCommand(
        () -> {
          UUID id = UUID.fromString(gameId);
          gameApi.markPlayerReady(id, new MarkPlayerReadyRequest().playerId(context.getPlayerId()));
          return viewResolver.markReadyView().render(new MarkReadyViewModel(id));
        });
  }

  @ShellMethod("Fire at a coordinate")
  public String fire(String gameId, int x, int y) {
    return executeCommand(
        () -> {
          UUID id = UUID.fromString(gameId);
          var result =
              gameApi.shootAtCoordinate(
                  id,
                  new ShootAtCoordinateRequest()
                      .playerId(context.getPlayerId())
                      .target(new Coordinate().x(x).y(y)));
          return viewResolver.fireShotView().render(new FireShotViewModel(id, result, x, y));
        });
  }

  @ShellMethod("Place fleet randomly")
  public String placeFleetRandom(UUID gameId) {
    return executeCommand(
        () -> {
          var ships = fleetGenerator.generateFromRandom();
          var fleetDeployment =
              gameApi.placeFleet(
                  gameId, new PlaceFleetRequest().playerId(context.getPlayerId()).ships(ships));
          return viewResolver
              .placeFleetView()
              .render(new PlaceFleetViewModel(fleetDeployment.getShips()));
        });
  }

  @ShellMethod("Place fleet from a JSON file")
  public String placeFleet(UUID gameId, String fleetFilePath) {
    return executeCommand(
        () -> {
          var ships = fleetGenerator.generateFromFile(fleetFilePath);
          var fleetDeployment =
              gameApi.placeFleet(
                  gameId, new PlaceFleetRequest().playerId(context.getPlayerId()).ships(ships));
          return viewResolver
              .placeFleetView()
              .render(new PlaceFleetViewModel(fleetDeployment.getShips()));
        });
  }

  private String executeCommand(Supplier<String> command) {
    String RED = "\u001B[31m";
    String RESET = "\u001B[0m";
    try {
      return command.get();
    } catch (GameApiException ex) {
      return RED + "API Error: " + ex.getMessage() + RESET;
    } catch (Exception ex) {
      return RED + "Unexpected error: " + ex.getMessage() + RESET;
    }
  }
}
