package dev.thiagooliveira.deepstrike.infrastructure.api.mapper;

import dev.thiagooliveira.deepstrike.application.command.*;
import dev.thiagooliveira.deepstrike.domain.GameId;
import dev.thiagooliveira.deepstrike.domain.PlayerId;
import dev.thiagooliveira.deepstrike.infrastructure.api.dto.*;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameMapper {

  GameResponse toResponse(dev.thiagooliveira.deepstrike.domain.Game game);

  CreateGameCommand toCreateGameCommand(CreateGameRequest createGameRequest);

  JoinGameCommand toJoinGameCommand(UUID gameId, JoinGameRequest joinGameRequest);

  PlaceFleetCommand toPlaceFleetCommand(UUID gameId, PlaceFleetRequest placeFleetRequest);

  MarkReadyCommand toMarkReadyCommand(UUID gameId, JoinGameRequest joinGameRequest);

  FireShotCommand toFireShotCommand(UUID gameId, ShootAtCoordinateRequest shootAtCoordinateRequest);

  default UUID map(GameId value) {
    return value.value();
  }

  default UUID map(PlayerId value) {
    return value != null ? value.value() : null;
  }

  default PlayerId mapPlayerId(UUID value) {
    return value != null ? new PlayerId(value) : null;
  }

  default GameId mapGameId(UUID value) {
    return value != null ? new GameId(value) : null;
  }
}
