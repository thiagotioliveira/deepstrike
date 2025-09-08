package dev.thiagooliveira.deepstrike.infrastructure.api;

import dev.thiagooliveira.deepstrike.application.command.GetGameByIdCommand;
import dev.thiagooliveira.deepstrike.application.command.GetGamesCommand;
import dev.thiagooliveira.deepstrike.application.usecase.*;
import dev.thiagooliveira.deepstrike.domain.GameId;
import dev.thiagooliveira.deepstrike.domain.PlayerId;
import dev.thiagooliveira.deepstrike.infrastructure.api.dto.*;
import dev.thiagooliveira.deepstrike.infrastructure.api.mapper.GameMapper;
import dev.thiagooliveira.deepstrike.infrastructure.game.GameService;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameApi implements DefaultApi {

  private final GameService gameService;
  private final GameMapper gameMapper;

  public GameApi(GameService gameService, GameMapper gameMapper) {
    this.gameService = gameService;
    this.gameMapper = gameMapper;
  }

  @Override
  public ResponseEntity<GameCreatedResponse> createGame(CreateGameRequest createGameRequest) {
    var game = gameService.createGame(gameMapper.toCreateGameCommand(createGameRequest));
    return ResponseEntity.created(URI.create("/api/games/" + game.getId().value()))
        .body(gameMapper.toResponse(game));
  }

  @Override
  public ResponseEntity<GameDetailResponse> detail(UUID gameId) {
    var game =
        gameService
            .getGameById(new GetGameByIdCommand(new GameId(gameId)))
            .orElseThrow(() -> new IllegalArgumentException("Game not found: " + gameId));
    return ResponseEntity.ok(gameMapper.toDetailResponse(game));
  }

  @Override
  public ResponseEntity<Void> joinGame(UUID gameId, JoinGameRequest joinGameRequest) {
    gameService.joinGame(gameMapper.toJoinGameCommand(gameId, joinGameRequest));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<List<GameSummaryResponse>> listGames(String playerId) {
    return ResponseEntity.ok(
        gameService.findAll(new GetGamesCommand(new PlayerId(playerId))).stream()
            .map(gameMapper::toSummaryResponse)
            .toList());
  }

  @Override
  public ResponseEntity<Void> markPlayerReady(
      UUID gameId, MarkPlayerReadyRequest markPlayerReadyRequest) {
    gameService.markPlayerReady(gameMapper.toMarkReadyCommand(gameId, markPlayerReadyRequest));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<FleetDeploymentResponse> placeFleet(
      UUID gameId, PlaceFleetRequest placeFleetRequest) {
    return ResponseEntity.ok(
        gameMapper.toFleetDeploymentResponse(
            gameService.placeFleet(gameMapper.toPlaceFleetCommand(gameId, placeFleetRequest))));
  }

  @Override
  public ResponseEntity<ShotResultResponse> shootAtCoordinate(
      UUID gameId, ShootAtCoordinateRequest shootAtCoordinateRequest) {
    return ResponseEntity.ok(
        gameMapper.toShotResultResponse(
            gameService.fireShot(gameMapper.toFireShotCommand(gameId, shootAtCoordinateRequest))));
  }
}
