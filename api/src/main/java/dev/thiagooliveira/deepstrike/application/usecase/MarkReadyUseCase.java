package dev.thiagooliveira.deepstrike.application.usecase;

import dev.thiagooliveira.deepstrike.application.command.MarkReadyCommand;
import dev.thiagooliveira.deepstrike.application.eventstore.EventStore;
import dev.thiagooliveira.deepstrike.domain.Game;

public class MarkReadyUseCase {

  private final EventStore eventStore;

  public MarkReadyUseCase(EventStore eventStore) {
    this.eventStore = eventStore;
  }

  public Game handle(MarkReadyCommand command) {
    var pastEvents = eventStore.load(command.gameId().value());
    if (pastEvents.isEmpty()) {
      throw new IllegalArgumentException("Game not found: " + command.gameId());
    }

    Game game = Game.rehydrate(pastEvents);

    game.markReady(command.playerId());

    var newEvents = game.getPendingEvents();

    eventStore.append(game.getId().value(), newEvents, game.getVersion() - newEvents.size());

    game.markEventsCommitted();
    return game;
  }
}
