package dev.thiagooliveira.deepstrike.application.command.model;

import dev.thiagooliveira.deepstrike.infrastructure.client.dto.ShotResultResponse;
import java.util.UUID;

public record FireShotViewModel(UUID gameId, ShotResultResponse result, int x, int y) {}
