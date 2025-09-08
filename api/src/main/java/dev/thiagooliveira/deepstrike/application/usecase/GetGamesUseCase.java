package dev.thiagooliveira.deepstrike.application.usecase;

import dev.thiagooliveira.deepstrike.application.command.GetGamesCommand;
import dev.thiagooliveira.deepstrike.application.port.outbound.GameSummaryRepository;
import dev.thiagooliveira.deepstrike.domain.GameSummary;
import java.util.List;

public class GetGamesUseCase {

  private final GameSummaryRepository repository;

  public GetGamesUseCase(GameSummaryRepository repository) {
    this.repository = repository;
  }

  public List<GameSummary> handle(GetGamesCommand command) {
    return repository.findAll(command.playerId());
  }
}
