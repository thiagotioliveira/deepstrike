package dev.thiagooliveira.deepstrike.application.command.view;

import dev.thiagooliveira.deepstrike.application.command.model.JoinGameViewModel;

public class JoinGameView implements View<JoinGameViewModel> {
  @Override
  public String render(JoinGameViewModel model) {
    return "You joined the game " + model.gameId();
  }
}
