package dev.thiagooliveira.deepstrike.application.usecase;

import dev.thiagooliveira.deepstrike.application.command.PlaceFleetCommand;
import dev.thiagooliveira.deepstrike.application.port.outbound.EventStore;
import dev.thiagooliveira.deepstrike.domain.FleetDeployment;
import dev.thiagooliveira.deepstrike.domain.Game;
import org.springframework.context.ApplicationEventPublisher;

public class PlaceFleetUseCase {

  private final EventStore eventStore;
  private final ApplicationEventPublisher publisher;

  public PlaceFleetUseCase(EventStore eventStore, ApplicationEventPublisher publisher) {
    this.eventStore = eventStore;
    this.publisher = publisher;
  }

  public FleetDeployment handle(PlaceFleetCommand command) {
    var pastEvents = eventStore.load(command.gameId().value());
    if (pastEvents.isEmpty()) {
      throw new IllegalArgumentException("Game not found: " + command.gameId().value());
    }

    Game game = Game.rehydrate(pastEvents);
    game.placeFleet(command.playerId(), command.ships());

    var newEvents = game.getPendingEvents();

    eventStore.append(game.getId().value(), newEvents, game.getVersion() - newEvents.size());
    newEvents.forEach(publisher::publishEvent);

    game.markEventsCommitted();
    return new FleetDeployment(
        game.getBoards().get(command.playerId()).getShips().stream()
            .map(s -> new FleetDeployment.ShipDeployment(s.getType().name(), s.getFootprint()))
            .toList());
  }
}
