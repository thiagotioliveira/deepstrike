package dev.thiagooliveira.deepstrike.application.command;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.thiagooliveira.deepstrike.application.GameApiClientFactory;
import dev.thiagooliveira.deepstrike.application.context.PlayerContext;
import dev.thiagooliveira.deepstrike.infrastructure.client.api.DefaultApi;
import dev.thiagooliveira.deepstrike.infrastructure.client.dto.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class GameCommands {

  private final DefaultApi gameApi;
  private final PlayerContext context;
  private final ObjectMapper objectMapper;

  public GameCommands(
      GameApiClientFactory factory, PlayerContext context, ObjectMapper objectMapper) {
    this.gameApi = factory.create("http://localhost:8080");
    this.context = context;
    this.objectMapper = objectMapper;
  }

  @ShellMethod("Create a new game")
  public String createGame() {
    var response = gameApi.createGame(new CreateGameRequest().hostPlayerId(context.getPlayerId()));
    return "Game had created with ID: " + response.getId();
  }

  @ShellMethod("List games")
  public String listGames() {
    var games = gameApi.listGames(context.getPlayerId());
    StringBuilder sb = new StringBuilder();
    for (var g : games) {
      sb.append("ID=")
          .append(g.getId())
          .append(" | Status=")
          .append(g.getStatus())
          .append(" | CreatedAt=")
          .append(g.getCreatedAt())
          .append("\n");
    }
    return sb.toString();
  }

  @ShellMethod("Entra em um jogo existente")
  public String joinGame(String gameId) {
    UUID id = UUID.fromString(gameId);
    var game = gameApi.joinGame(id, new JoinGameRequest().playerId(context.getPlayerId()));
    return "Você entrou no jogo " + game.getId();
  }

  @ShellMethod("Marca jogador como pronto")
  public String ready(String gameId) {
    UUID id = UUID.fromString(gameId);
    var game =
        gameApi.markPlayerReady(id, new MarkPlayerReadyRequest().playerId(context.getPlayerId()));
    return "Você está pronto!";
  }

  @ShellMethod("Atira em uma coordenada")
  public String fire(String gameId, int x, int y) {
    UUID id = UUID.fromString(gameId);
    var game =
        gameApi.shootAtCoordinate(
            id,
            new ShootAtCoordinateRequest()
                .playerId(context.getPlayerId())
                .target(new Coordinate().x(x).y(y)));
    return "Tiro disparado em (" + x + "," + y + ")";
  }

  @ShellMethod("Coloca a frota a partir de um ficheiro JSON")
  public String placeFleet(UUID gameId, String fleetFilePath) {
    try {
      if (!Files.exists(Path.of(fleetFilePath))) {
        throw new IllegalStateException("Fleet file not found at " + fleetFilePath);
      }
      String json = Files.readString(Path.of(fleetFilePath));
      List<Map<String, Object>> fleet = objectMapper.readValue(json, new TypeReference<>() {});
      List<Ship> ships = new ArrayList<>();
      for (Map<String, Object> ship : fleet) {
        ships.add(
            new Ship()
                .orientation(
                    ship.get("orientation").equals("horizontal")
                        ? Ship.OrientationEnum.HORIZONTAL
                        : Ship.OrientationEnum.VERTICAL)
                .type(
                    switch ((String) ship.get("type")) {
                      case "BATTLESHIP" -> Ship.TypeEnum.BATTLESHIP;
                      case "CARRIER" -> Ship.TypeEnum.CARRIER;
                      case "PATROL_BOAT" -> Ship.TypeEnum.PATROL_BOAT;
                      case "DESTROYER" -> Ship.TypeEnum.DESTROYER;
                      case "SUBMARINE" -> Ship.TypeEnum.SUBMARINE;
                      default ->
                          throw new IllegalArgumentException(
                              "Unknown ship type: " + ship.get("type"));
                    })
                .bow(new Coordinate().x((int) ship.get("x")).y((int) ship.get("y"))));
      }
      var game =
          gameApi.placeFleet(
              gameId, new PlaceFleetRequest().playerId(context.getPlayerId()).ships(ships));
      return "Frota colocada (" + ships.size() + " navios) a partir de " + fleetFilePath;
    } catch (IOException e) {
      throw new RuntimeException("Failed to read fleet file", e);
    }
  }
}
