package dev.thiagooliveira.deepstrike.application.command;

import dev.thiagooliveira.deepstrike.domain.GameId;
import dev.thiagooliveira.deepstrike.domain.PlayerId;
import java.util.Optional;

public record GetGameByIdCommand(GameId gameId, PlayerId playerId, Optional<Integer> version) {
  public GetGameByIdCommand(GameId gameId, PlayerId playerId) {
    this(gameId, playerId, Optional.empty());
  }
}
