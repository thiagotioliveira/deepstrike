package dev.thiagooliveira.deepstrike.application.usecase;

import dev.thiagooliveira.deepstrike.application.command.GetGameByIdCommand;
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
    var events = eventStore.load(command.gameId().value());
    if (events.isEmpty()) return Optional.empty();
    return Optional.of(new GameDetail(Game.rehydrate(events)));
  }
}
