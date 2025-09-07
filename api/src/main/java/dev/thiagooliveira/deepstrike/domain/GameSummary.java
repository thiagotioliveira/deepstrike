package dev.thiagooliveira.deepstrike.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record GameSummary(UUID id, GameStatus status, OffsetDateTime createdAt) {}
