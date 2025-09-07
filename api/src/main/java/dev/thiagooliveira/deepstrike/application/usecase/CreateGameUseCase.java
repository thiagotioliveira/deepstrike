package dev.thiagooliveira.deepstrike.application.usecase;

import dev.thiagooliveira.deepstrike.application.command.CreateGameCommand;
import dev.thiagooliveira.deepstrike.application.port.outbound.EventStore;
import dev.thiagooliveira.deepstrike.domain.Game;
import dev.thiagooliveira.deepstrike.domain.rule.Ruleset;
import org.springframework.context.ApplicationEventPublisher;

public class CreateGameUseCase {

  private final EventStore eventStore;
  private final ApplicationEventPublisher publisher;

  public CreateGameUseCase(EventStore eventStore, ApplicationEventPublisher publisher) {
    this.eventStore = eventStore;
    this.publisher = publisher;
  }

  public Game handle(CreateGameCommand command) {
    var rules = Ruleset.formation2002();
    var game = Game.create(command.hostPlayerId(), rules);
    var events = game.getPendingEvents();

    eventStore.append(game.getId().value(), events, game.getVersion() - events.size());
    events.forEach(publisher::publishEvent);

    game.markEventsCommitted();

    return game;
  }
}
