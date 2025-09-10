package dev.thiagooliveira.deepstrike.domain;

public enum GameStatus {
  /** Created, waiting for players. */
  OPEN("Open"),

  /** Two players present, fleet positioning phase. */
  SETUP("Setup"),

  /** Game in progress (shooting turns). */
  IN_PROGRESS("In Progress"),

  /** Normally finished (player victory). */
  FINISHED("Finished"),

  /** Abnormally terminated (timeout, withdrawal, error). */
  ABORTED("Aborted");

  private final String displayName;

  private GameStatus(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
