package dev.thiagooliveira.deepstrike.application.usecase;

import dev.thiagooliveira.deepstrike.application.command.GetGameByIdCommand;
import dev.thiagooliveira.deepstrike.application.dto.GameDetail;
import dev.thiagooliveira.deepstrike.application.exception.ApplicationException;
import dev.thiagooliveira.deepstrike.application.port.outbound.EventStore;
import dev.thiagooliveira.deepstrike.domain.Game;
import java.util.Optional;

public class GetGameByIdUseCase {

  private final EventStore eventStore;

  public GetGameByIdUseCase(EventStore eventStore) {
    this.eventStore = eventStore;
  }

  public Optional<GameDetail> handle(GetGameByIdCommand command) {
    var events =
        command.version().isEmpty()
            ? eventStore.load(command.gameId().value())
            : eventStore.load(command.gameId().value(), command.version().get());
    if (events.isEmpty()) return Optional.empty();

    if (command.version().isPresent() && events.getLast().version() != command.version().get()) {
      throw ApplicationException.badRequest("invalid version");
    }

    Game game = Game.rehydrate(events);
    if (!game.isPlayerInGame(command.playerId())) {
      throw ApplicationException.notFound("game not found");
    }
    return Optional.of(new GameDetail(game, command.playerId()));
  }
}
