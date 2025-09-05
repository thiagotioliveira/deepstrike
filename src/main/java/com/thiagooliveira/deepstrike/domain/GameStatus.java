package com.thiagooliveira.deepstrike.domain;

public enum GameStatus {
  /** Created, waiting for players. */
  OPEN,

  /** Two players present, fleet positioning phase. */
  SETUP,

  /** Game in progress (shooting turns). */
  IN_PROGRESS,

  /** Normally finished (player victory). */
  FINISHED,

  /** Abnormally terminated (timeout, withdrawal, error). */
  ABORTED
}
