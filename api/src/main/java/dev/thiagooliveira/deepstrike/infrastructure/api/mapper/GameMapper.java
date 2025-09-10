package dev.thiagooliveira.deepstrike.infrastructure.api.mapper;

import dev.thiagooliveira.deepstrike.application.command.*;
import dev.thiagooliveira.deepstrike.application.dto.FleetDeployment;
import dev.thiagooliveira.deepstrike.application.dto.GameDetail;
import dev.thiagooliveira.deepstrike.application.dto.GameSummary;
import dev.thiagooliveira.deepstrike.domain.GameId;
import dev.thiagooliveira.deepstrike.domain.PlayerId;
import dev.thiagooliveira.deepstrike.infrastructure.api.dto.*;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GameMapper {

  @Mapping(target = "player1.id", source = "game.player1.id.value")
  @Mapping(target = "player1.board", source = "game.player1.board")
  @Mapping(target = "player2.id", source = "game.player2.id.value")
  @Mapping(target = "player2.board", source = "game.player2.board")
  @Mapping(target = "status", source = "game.status.displayName")
  GameDetailResponse toDetailResponse(GameDetail game);

  GameSummaryResponse toSummaryResponse(GameSummary gameSummary);

  GameCreatedResponse toResponse(dev.thiagooliveira.deepstrike.domain.Game game);

  FleetDeploymentResponse toFleetDeploymentResponse(FleetDeployment fleetDeployment);

  @Mapping(target = "result", source = "shotResult")
  ShotResultResponse toShotResultResponse(
      dev.thiagooliveira.deepstrike.domain.board.ShotResult shotResult);

  CreateGameCommand toCreateGameCommand(CreateGameRequest createGameRequest);

  JoinGameCommand toJoinGameCommand(UUID gameId, JoinGameRequest joinGameRequest);

  PlaceFleetCommand toPlaceFleetCommand(UUID gameId, PlaceFleetRequest placeFleetRequest);

  MarkReadyCommand toMarkReadyCommand(UUID gameId, MarkPlayerReadyRequest markPlayerReadyRequest);

  FireShotCommand toFireShotCommand(UUID gameId, ShootAtCoordinateRequest shootAtCoordinateRequest);

  default UUID map(GameId value) {
    return value.value();
  }

  default String map(PlayerId value) {
    return value != null ? value.value() : null;
  }

  default PlayerId mapPlayerId(String value) {
    return value != null ? new PlayerId(value) : null;
  }

  default GameId mapGameId(UUID value) {
    return value != null ? new GameId(value) : null;
  }
}
