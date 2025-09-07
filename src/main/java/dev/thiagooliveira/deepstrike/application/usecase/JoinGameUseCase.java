package dev.thiagooliveira.deepstrike.application.usecase;

import dev.thiagooliveira.deepstrike.application.command.JoinGameCommand;
import dev.thiagooliveira.deepstrike.application.eventstore.EventStore;
import dev.thiagooliveira.deepstrike.domain.Game;

public class JoinGameUseCase {

  private final EventStore eventStore;

  public JoinGameUseCase(EventStore eventStore) {
    this.eventStore = eventStore;
  }

  public Game handle(JoinGameCommand command) {
    var pastEvents = eventStore.load(command.gameId().value());
    if (pastEvents.isEmpty()) {
      throw new IllegalArgumentException("Game not found: " + command.gameId().value());
    }

    Game game = Game.rehydrate(pastEvents);
    game.join(command.playerId());
    var newEvents = game.getPendingEvents();
    eventStore.append(game.getId().value(), newEvents, game.getVersion() - newEvents.size());

    game.markEventsCommitted();
    return game;
  }
}
