package dev.thiagooliveira.deepstrike.infrastructure.game;

import dev.thiagooliveira.deepstrike.application.eventstore.EventStore;
import dev.thiagooliveira.deepstrike.application.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameConfig {

  @Bean
  public CreateGameUseCase createGameUseCase(EventStore eventStore) {
    return new CreateGameUseCase(eventStore);
  }

  @Bean
  public JoinGameUseCase joinGameUseCase(EventStore eventStore) {
    return new JoinGameUseCase(eventStore);
  }

  @Bean
  public PlaceFleetUseCase placeFleetUseCase(EventStore eventStore) {
    return new PlaceFleetUseCase(eventStore);
  }

  @Bean
  public MarkReadyUseCase markReadyUseCase(EventStore eventStore) {
    return new MarkReadyUseCase(eventStore);
  }

  @Bean
  public FireShotUseCase fireShotUseCase(EventStore eventStore) {
    return new FireShotUseCase(eventStore);
  }
}
