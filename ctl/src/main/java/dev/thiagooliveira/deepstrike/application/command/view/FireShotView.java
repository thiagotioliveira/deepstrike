package dev.thiagooliveira.deepstrike.application.command.view;

import dev.thiagooliveira.deepstrike.application.command.model.FireShotViewModel;

public class FireShotView implements View<FireShotViewModel> {
  @Override
  public String render(FireShotViewModel model) {
    return "Shot fired at ("
        + model.x()
        + ","
        + model.y()
        + ") with result: "
        + model.result().getResult();
  }
}
