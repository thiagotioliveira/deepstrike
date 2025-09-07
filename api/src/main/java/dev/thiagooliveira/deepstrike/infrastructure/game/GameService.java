package dev.thiagooliveira.deepstrike.infrastructure.game;

import dev.thiagooliveira.deepstrike.application.command.*;
import dev.thiagooliveira.deepstrike.application.port.outbound.GameSummaryRepository;
import dev.thiagooliveira.deepstrike.application.usecase.*;
import dev.thiagooliveira.deepstrike.domain.Game;
import dev.thiagooliveira.deepstrike.domain.GameSummary;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {

  private final CreateGameUseCase createGameUseCase;
  private final JoinGameUseCase joinGameUseCase;
  private final PlaceFleetUseCase placeFleetUseCase;
  private final MarkReadyUseCase markReadyUseCase;
  private final FireShotUseCase fireShotUseCase;
  private final GameSummaryRepository gameSummaryRepository;

  public GameService(
      CreateGameUseCase createGameUseCase,
      JoinGameUseCase joinGameUseCase,
      PlaceFleetUseCase placeFleetUseCase,
      MarkReadyUseCase markReadyUseCase,
      FireShotUseCase fireShotUseCase,
      GameSummaryRepository gameSummaryRepository) {
    this.createGameUseCase = createGameUseCase;
    this.joinGameUseCase = joinGameUseCase;
    this.placeFleetUseCase = placeFleetUseCase;
    this.markReadyUseCase = markReadyUseCase;
    this.fireShotUseCase = fireShotUseCase;
    this.gameSummaryRepository = gameSummaryRepository;
  }

  public List<GameSummary> findAll() {
    return gameSummaryRepository.findAll();
  }

  @Transactional
  public Game createGame(CreateGameCommand command) {
    return createGameUseCase.handle(command);
  }

  @Transactional
  public Game joinGame(JoinGameCommand command) {
    return joinGameUseCase.handle(command);
  }

  @Transactional
  public Game placeFleet(PlaceFleetCommand command) {
    return placeFleetUseCase.handle(command);
  }

  @Transactional
  public Game markPlayerReady(MarkReadyCommand command) {
    return markReadyUseCase.handle(command);
  }

  @Transactional
  public Game fireShot(FireShotCommand command) {
    return fireShotUseCase.handle(command);
  }
}
