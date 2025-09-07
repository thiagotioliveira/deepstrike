package dev.thiagooliveira.deepstrike.infrastructure.api;

import dev.thiagooliveira.deepstrike.application.usecase.*;
import dev.thiagooliveira.deepstrike.infrastructure.api.dto.*;
import dev.thiagooliveira.deepstrike.infrastructure.api.mapper.GameMapper;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameApi implements DefaultApi {

  private final CreateGameUseCase createGameUseCase;
  private final JoinGameUseCase joinGameUseCase;
  private final PlaceFleetUseCase placeFleetUseCase;
  private final MarkReadyUseCase markReadyUseCase;
  private final FireShotUseCase fireShotUseCase;
  private final GameMapper gameMapper;

  public GameApi(
      CreateGameUseCase createGameUseCase,
      JoinGameUseCase joinGameUseCase,
      PlaceFleetUseCase placeFleetUseCase,
      MarkReadyUseCase markReadyUseCase,
      FireShotUseCase fireShotUseCase,
      GameMapper gameMapper) {
    this.createGameUseCase = createGameUseCase;
    this.joinGameUseCase = joinGameUseCase;
    this.placeFleetUseCase = placeFleetUseCase;
    this.markReadyUseCase = markReadyUseCase;
    this.fireShotUseCase = fireShotUseCase;
    this.gameMapper = gameMapper;
  }

  @Override
  public ResponseEntity<GameResponse> createGame(CreateGameRequest createGameRequest) {
    var game = createGameUseCase.handle(gameMapper.toCreateGameCommand(createGameRequest));
    return ResponseEntity.created(URI.create("/api/games/" + game.getId().value()))
        .body(gameMapper.toResponse(game));
  }

  @Override
  public ResponseEntity<GameResponse> joinGame(UUID gameId, JoinGameRequest joinGameRequest) {
    return ResponseEntity.ok(
        gameMapper.toResponse(
            joinGameUseCase.handle(gameMapper.toJoinGameCommand(gameId, joinGameRequest))));
  }

  @Override
  public ResponseEntity<List<GameSummaryResponse>> listGames() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public ResponseEntity<GameResponse> markPlayerReady(
      UUID gameId, JoinGameRequest joinGameRequest) {
    return ResponseEntity.ok(
        gameMapper.toResponse(
            markReadyUseCase.handle(gameMapper.toMarkReadyCommand(gameId, joinGameRequest))));
  }

  @Override
  public ResponseEntity<GameResponse> placeFleet(UUID gameId, PlaceFleetRequest placeFleetRequest) {
    return ResponseEntity.ok(
        gameMapper.toResponse(
            placeFleetUseCase.handle(gameMapper.toPlaceFleetCommand(gameId, placeFleetRequest))));
  }

  @Override
  public ResponseEntity<GameResponse> shootAtCoordinate(
      UUID gameId, ShootAtCoordinateRequest shootAtCoordinateRequest) {
    return ResponseEntity.ok(
        gameMapper.toResponse(
            fireShotUseCase.handle(
                gameMapper.toFireShotCommand(gameId, shootAtCoordinateRequest))));
  }
}
