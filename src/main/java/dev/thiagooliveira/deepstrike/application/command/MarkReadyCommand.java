package dev.thiagooliveira.deepstrike.application.command;

import dev.thiagooliveira.deepstrike.domain.GameId;
import dev.thiagooliveira.deepstrike.domain.PlayerId;

public record MarkReadyCommand(GameId gameId, PlayerId playerId) {}
