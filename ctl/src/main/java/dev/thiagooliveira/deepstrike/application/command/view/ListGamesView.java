package dev.thiagooliveira.deepstrike.application.command.view;

import dev.thiagooliveira.deepstrike.application.command.model.ListGamesViewModel;

public class ListGamesView implements View<ListGamesViewModel> {

  @Override
  public String render(ListGamesViewModel model) {
    if (model.list().isEmpty()) {
      return "No list found";
    }
    StringBuilder sb = new StringBuilder();
    for (var g : model.list()) {
      sb.append("ID=")
          .append(g.getId())
          .append(" | Status=")
          .append(g.getStatus())
          .append(" | CreatedAt=")
          .append(g.getCreatedAt())
          .append("\n");
    }
    return sb.toString();
  }
}
