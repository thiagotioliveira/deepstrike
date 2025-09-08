package dev.thiagooliveira.deepstrike.application.command.view;

import org.springframework.stereotype.Component;

@Component
public class ViewResolver {

  private final ListGamesView listGamesView = new ListGamesView();
  private final GameDetailView gameDetailView = new GameDetailView();
  private final CreateGameView createGameView = new CreateGameView();
  private final ContextView contextView = new ContextView();
  private final JoinGameView joinGameView = new JoinGameView();
  private final MarkReadyView markReadyView = new MarkReadyView();
  private final FireShotView fireShotView = new FireShotView();
  private final PlaceFleetView placeFleetView = new PlaceFleetView();

  private View resolve(String viewName) {
    return switch (viewName) {
      case "list" -> listGamesView;
      case "detail" -> gameDetailView;
      case "create" -> createGameView;
      case "context" -> contextView;
      case "join" -> joinGameView;
      case "markReady" -> markReadyView;
      case "fireShot" -> fireShotView;
      case "placeFleet" -> placeFleetView;
      default -> throw new IllegalArgumentException("Unknown view: " + viewName);
    };
  }

  public ListGamesView listView() {
    return (ListGamesView) resolve("list");
  }

  public GameDetailView gameDetailView() {
    return (GameDetailView) resolve("detail");
  }

  public CreateGameView createGameView() {
    return (CreateGameView) resolve("create");
  }

  public ContextView contextView() {
    return (ContextView) resolve("context");
  }

  public JoinGameView joinGameView() {
    return (JoinGameView) resolve("join");
  }

  public MarkReadyView markReadyView() {
    return (MarkReadyView) resolve("markReady");
  }

  public FireShotView fireShotView() {
    return (FireShotView) resolve("fireShot");
  }

  public PlaceFleetView placeFleetView() {
    return (PlaceFleetView) resolve("placeFleet");
  }
}
