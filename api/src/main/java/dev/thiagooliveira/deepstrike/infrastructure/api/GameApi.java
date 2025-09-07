package dev.thiagooliveira.deepstrike.infrastructure.api;

import dev.thiagooliveira.deepstrike.application.usecase.*;
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
  public ResponseEntity<GameResponse> createGame(CreateGameRequest createGameRequest) {
    var game = gameService.createGame(gameMapper.toCreateGameCommand(createGameRequest));
    return ResponseEntity.created(URI.create("/api/games/" + game.getId().value()))
        .body(gameMapper.toResponse(game));
  }

  @Override
  public ResponseEntity<GameResponse> joinGame(UUID gameId, JoinGameRequest joinGameRequest) {
    return ResponseEntity.ok(
        gameMapper.toResponse(
            gameService.joinGame(gameMapper.toJoinGameCommand(gameId, joinGameRequest))));
  }

  @Override
  public ResponseEntity<List<GameSummaryResponse>> listGames(UUID playerId) {
    return ResponseEntity.ok(
        gameService.findAll(new PlayerId(playerId)).stream()
            .map(gameMapper::toSummaryResponse)
            .toList());
  }

  @Override
  public ResponseEntity<GameResponse> markPlayerReady(
      UUID gameId, MarkPlayerReadyRequest markPlayerReadyRequest) {
    return ResponseEntity.ok(
        gameMapper.toResponse(
            gameService.markPlayerReady(
                gameMapper.toMarkReadyCommand(gameId, markPlayerReadyRequest))));
  }

  @Override
  public ResponseEntity<GameResponse> placeFleet(UUID gameId, PlaceFleetRequest placeFleetRequest) {
    return ResponseEntity.ok(
        gameMapper.toResponse(
            gameService.placeFleet(gameMapper.toPlaceFleetCommand(gameId, placeFleetRequest))));
  }

  @Override
  public ResponseEntity<GameResponse> shootAtCoordinate(
      UUID gameId, ShootAtCoordinateRequest shootAtCoordinateRequest) {
    return ResponseEntity.ok(
        gameMapper.toResponse(
            gameService.fireShot(gameMapper.toFireShotCommand(gameId, shootAtCoordinateRequest))));
  }
}
