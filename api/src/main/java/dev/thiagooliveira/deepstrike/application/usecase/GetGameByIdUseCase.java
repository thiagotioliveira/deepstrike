package dev.thiagooliveira.deepstrike.application.usecase;

import dev.thiagooliveira.deepstrike.application.command.GetGameByIdCommand;
import dev.thiagooliveira.deepstrike.application.exception.ApplicationException;
import dev.thiagooliveira.deepstrike.application.port.outbound.EventStore;
import dev.thiagooliveira.deepstrike.domain.Game;
import dev.thiagooliveira.deepstrike.domain.GameDetail;
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
    if (command.version().isPresent()) {
      if (events.getLast().version() != command.version().get()) {
        throw ApplicationException.badRequest("invalid version");
      }
    }
    if (events.isEmpty()) return Optional.empty();
    return Optional.of(new GameDetail(Game.rehydrate(events)));
  }
}
