package dev.thiagooliveira.deepstrike.application.usecase;

import dev.thiagooliveira.deepstrike.application.command.PlaceFleetCommand;
import dev.thiagooliveira.deepstrike.application.eventstore.EventStore;
import dev.thiagooliveira.deepstrike.domain.Game;

public class PlaceFleetUseCase {

  private final EventStore eventStore;

  public PlaceFleetUseCase(EventStore eventStore) {
    this.eventStore = eventStore;
  }

  public Game handle(PlaceFleetCommand command) {
    var pastEvents = eventStore.load(command.gameId().value());
    if (pastEvents.isEmpty()) {
      throw new IllegalArgumentException("Game not found: " + command.gameId().value());
    }

    Game game = Game.rehydrate(pastEvents);
    game.placeFleet(command.playerId(), command.ships());

    var newEvents = game.getPendingEvents();

    eventStore.append(game.getId().value(), newEvents, game.getVersion() - newEvents.size());

    game.markEventsCommitted();
    return game;
  }
}
