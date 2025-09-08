package dev.thiagooliveira.deepstrike.application.command.model;

import dev.thiagooliveira.deepstrike.infrastructure.client.dto.GameSummaryResponse;
import java.util.List;

public record ListGamesViewModel(List<GameSummaryResponse> list) {}
