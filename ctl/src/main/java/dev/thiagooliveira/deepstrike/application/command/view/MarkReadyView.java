package dev.thiagooliveira.deepstrike.application.command.view;

import dev.thiagooliveira.deepstrike.application.command.model.MarkReadyViewModel;

public class MarkReadyView implements View<MarkReadyViewModel> {
  @Override
  public String render(MarkReadyViewModel model) {
    return "You are ready!";
  }
}
