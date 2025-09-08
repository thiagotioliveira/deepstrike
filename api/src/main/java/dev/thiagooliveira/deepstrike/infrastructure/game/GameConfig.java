package dev.thiagooliveira.deepstrike.infrastructure.game;

import dev.thiagooliveira.deepstrike.application.port.outbound.EventStore;
import dev.thiagooliveira.deepstrike.application.port.outbound.GameSummaryRepository;
import dev.thiagooliveira.deepstrike.application.usecase.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameConfig {

  @Bean
  public CreateGameUseCase createGameUseCase(
      EventStore eventStore, ApplicationEventPublisher publisher) {
    return new CreateGameUseCase(eventStore, publisher);
  }

  @Bean
  public JoinGameUseCase joinGameUseCase(
      EventStore eventStore, ApplicationEventPublisher publisher) {
    return new JoinGameUseCase(eventStore, publisher);
  }

  @Bean
  public PlaceFleetUseCase placeFleetUseCase(
      EventStore eventStore, ApplicationEventPublisher publisher) {
    return new PlaceFleetUseCase(eventStore, publisher);
  }

  @Bean
  public MarkReadyUseCase markReadyUseCase(
      EventStore eventStore, ApplicationEventPublisher publisher) {
    return new MarkReadyUseCase(eventStore, publisher);
  }

  @Bean
  public FireShotUseCase fireShotUseCase(
      EventStore eventStore, ApplicationEventPublisher publisher) {
    return new FireShotUseCase(eventStore, publisher);
  }

  @Bean
  public GetGameByIdUseCase getGameByIdUseCase(EventStore eventStore) {
    return new GetGameByIdUseCase(eventStore);
  }

  @Bean
  public GetGamesUseCase getGamesUseCase(GameSummaryRepository repository) {
    return new GetGamesUseCase(repository);
  }
}
