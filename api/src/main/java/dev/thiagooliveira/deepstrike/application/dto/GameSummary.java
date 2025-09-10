package dev.thiagooliveira.deepstrike.application.dto;

import dev.thiagooliveira.deepstrike.domain.GameStatus;
import java.time.OffsetDateTime;
import java.util.UUID;

public record GameSummary(UUID id, GameStatus status, OffsetDateTime createdAt, int version) {}
