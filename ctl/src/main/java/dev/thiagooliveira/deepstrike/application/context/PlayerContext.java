package dev.thiagooliveira.deepstrike.application.context;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PlayerContext {

  @Value("${app.context-path-folder-name}")
  private String contextPathFolder;

  private Path CONTEXT_FILE;

  private UUID playerId;

  public PlayerContext() {}

  @PostConstruct
  public void init() {
    CONTEXT_FILE = Path.of(System.getProperty("user.home"), contextPathFolder, "context");
    this.playerId = loadOrCreate();
  }

  private UUID loadOrCreate() {
    System.out.println(CONTEXT_FILE);
    try {
      if (!Files.exists(CONTEXT_FILE)) {
        Files.createDirectories(CONTEXT_FILE.getParent());
        UUID newId = UUID.randomUUID();
        Files.writeString(CONTEXT_FILE, newId.toString(), StandardOpenOption.CREATE);
        return newId;
      }
      return UUID.fromString(Files.readString(CONTEXT_FILE).trim());
    } catch (IOException e) {
      throw new RuntimeException("Failed to initialize player context", e);
    }
  }

  public UUID getPlayerId() {
    return playerId;
  }
}
