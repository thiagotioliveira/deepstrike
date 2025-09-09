package dev.thiagooliveira.deepstrike.application.command;

import dev.thiagooliveira.deepstrike.domain.GameId;
import java.util.Optional;

public record GetGameByIdCommand(GameId gameId, Optional<Integer> version) {
  public GetGameByIdCommand(GameId gameId) {
    this(gameId, Optional.empty());
  }
}
