package dev.thiagooliveira.deepstrike.application.config;

import jakarta.annotation.PostConstruct;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppContext {

  @Value("${app.context.player-id}")
  private String playerId;

  @Value("${app.context.baseUrl}")
  private String baseUrl;

  public AppContext() {}

  @PostConstruct
  public void init() {
    Objects.requireNonNull(playerId, "Player id must be set");
    this.playerId = playerId.trim();
    Objects.requireNonNull(baseUrl, "base url must be set");
    this.baseUrl = baseUrl.trim();
  }

  public String getPlayerId() {
    return playerId;
  }

  public String getBaseUrl() {
    return baseUrl;
  }
}
