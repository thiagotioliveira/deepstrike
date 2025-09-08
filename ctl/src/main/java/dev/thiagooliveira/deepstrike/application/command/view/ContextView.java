package dev.thiagooliveira.deepstrike.application.command.view;

import dev.thiagooliveira.deepstrike.application.command.model.ContextViewModel;

public class ContextView implements View<ContextViewModel> {
  @Override
  public String render(ContextViewModel model) {
    return "Api (base url): " + model.baseUrl() + "\nPlayer ID: " + model.playerId();
  }
}
