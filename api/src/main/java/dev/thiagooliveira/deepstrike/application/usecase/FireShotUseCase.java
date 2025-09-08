package dev.thiagooliveira.deepstrike.application.usecase;

import dev.thiagooliveira.deepstrike.application.command.FireShotCommand;
import dev.thiagooliveira.deepstrike.application.port.outbound.EventStore;
import dev.thiagooliveira.deepstrike.domain.Game;
import dev.thiagooliveira.deepstrike.domain.board.ShotResult;
import org.springframework.context.ApplicationEventPublisher;

public class FireShotUseCase {

  private final EventStore eventStore;
  private final ApplicationEventPublisher publisher;

  public FireShotUseCase(EventStore eventStore, ApplicationEventPublisher publisher) {
    this.eventStore = eventStore;
    this.publisher = publisher;
  }

  public ShotResult handle(FireShotCommand command) {

    var pastEvents = eventStore.load(command.gameId().value());
    if (pastEvents.isEmpty()) {
      throw new IllegalArgumentException("Game not found: " + command.gameId());
    }

    Game game = Game.rehydrate(pastEvents);
    var result = game.fireShot(command.playerId(), command.target());

    var newEvents = game.getPendingEvents();

    eventStore.append(game.getId().value(), newEvents, game.getVersion() - newEvents.size());
    newEvents.forEach(publisher::publishEvent);

    game.markEventsCommitted();
    return result;
  }
}
