package dev.thiagooliveira.deepstrike.application.command.view;

import dev.thiagooliveira.deepstrike.application.command.model.CreateGameViewModel;

public class CreateGameView implements View<CreateGameViewModel> {
  @Override
  public String render(CreateGameViewModel model) {
    return "Game had created with ID: " + model.gameId();
  }
}
