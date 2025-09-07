package dev.thiagooliveira.deepstrike.application.usecase;

import dev.thiagooliveira.deepstrike.application.command.CreateGameCommand;
import dev.thiagooliveira.deepstrike.application.eventstore.EventStore;
import dev.thiagooliveira.deepstrike.domain.Game;
import dev.thiagooliveira.deepstrike.domain.rule.Ruleset;

public class CreateGameUseCase {

  private final EventStore eventStore;

  public CreateGameUseCase(EventStore eventStore) {
    this.eventStore = eventStore;
  }

  public Game handle(CreateGameCommand command) {
    var rules = Ruleset.formation2002();
    var game = Game.create(command.hostPlayerId(), rules);
    var events = game.getPendingEvents();

    eventStore.append(game.getId().value(), events, game.getVersion() - events.size());
    game.markEventsCommitted();

    return game;
  }
}
