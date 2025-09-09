package dev.thiagooliveira.deepstrike.application.usecase;

import dev.thiagooliveira.deepstrike.application.command.MarkReadyCommand;
import dev.thiagooliveira.deepstrike.application.exception.ApplicationException;
import dev.thiagooliveira.deepstrike.application.port.outbound.EventStore;
import dev.thiagooliveira.deepstrike.domain.Game;
import org.springframework.context.ApplicationEventPublisher;

public class MarkReadyUseCase {

  private final EventStore eventStore;
  private final ApplicationEventPublisher publisher;

  public MarkReadyUseCase(EventStore eventStore, ApplicationEventPublisher publisher) {
    this.eventStore = eventStore;
    this.publisher = publisher;
  }

  public Game handle(MarkReadyCommand command) {
    var pastEvents = eventStore.load(command.gameId().value());
    if (pastEvents.isEmpty()) {
      throw ApplicationException.notFound("game not found");
    }

    Game game = Game.rehydrate(pastEvents);

    game.markReady(command.playerId());

    var newEvents = game.getPendingEvents();

    eventStore.append(game.getId().value(), newEvents, game.getVersion() - newEvents.size());
    newEvents.forEach(publisher::publishEvent);

    game.markEventsCommitted();
    return game;
  }
}
