package dev.thiagooliveira.deepstrike.infrastructure.game;

import dev.thiagooliveira.deepstrike.application.command.*;
import dev.thiagooliveira.deepstrike.application.usecase.*;
import dev.thiagooliveira.deepstrike.domain.FleetDeployment;
import dev.thiagooliveira.deepstrike.domain.Game;
import dev.thiagooliveira.deepstrike.domain.GameDetail;
import dev.thiagooliveira.deepstrike.domain.GameSummary;
import dev.thiagooliveira.deepstrike.domain.board.ShotResult;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {

  private final CreateGameUseCase createGameUseCase;
  private final JoinGameUseCase joinGameUseCase;
  private final PlaceFleetUseCase placeFleetUseCase;
  private final MarkReadyUseCase markReadyUseCase;
  private final FireShotUseCase fireShotUseCase;
  private final GetGameByIdUseCase getGameByIdUseCase;
  private final GetGamesUseCase getGamesUseCase;

  public GameService(
      CreateGameUseCase createGameUseCase,
      JoinGameUseCase joinGameUseCase,
      PlaceFleetUseCase placeFleetUseCase,
      MarkReadyUseCase markReadyUseCase,
      FireShotUseCase fireShotUseCase,
      GetGameByIdUseCase getGameByIdUseCase,
      GetGamesUseCase getGamesUseCase) {
    this.createGameUseCase = createGameUseCase;
    this.joinGameUseCase = joinGameUseCase;
    this.placeFleetUseCase = placeFleetUseCase;
    this.markReadyUseCase = markReadyUseCase;
    this.fireShotUseCase = fireShotUseCase;
    this.getGameByIdUseCase = getGameByIdUseCase;
    this.getGamesUseCase = getGamesUseCase;
  }

  public List<GameSummary> findAll(GetGamesCommand command) {
    return getGamesUseCase.handle(command);
  }

  public Optional<GameDetail> getGameById(GetGameByIdCommand command) {
    return getGameByIdUseCase.handle(command);
  }

  @Transactional
  public Game createGame(CreateGameCommand command) {
    return createGameUseCase.handle(command);
  }

  @Transactional
  public void joinGame(JoinGameCommand command) {
    joinGameUseCase.handle(command);
  }

  @Transactional
  public FleetDeployment placeFleet(PlaceFleetCommand command) {
    return placeFleetUseCase.handle(command);
  }

  @Transactional
  public void markPlayerReady(MarkReadyCommand command) {
    markReadyUseCase.handle(command);
  }

  @Transactional
  public ShotResult fireShot(FireShotCommand command) {
    return fireShotUseCase.handle(command);
  }
}
