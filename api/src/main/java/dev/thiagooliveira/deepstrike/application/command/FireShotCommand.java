package dev.thiagooliveira.deepstrike.application.command;

import dev.thiagooliveira.deepstrike.domain.Coordinate;
import dev.thiagooliveira.deepstrike.domain.GameId;
import dev.thiagooliveira.deepstrike.domain.PlayerId;

public record FireShotCommand(GameId gameId, PlayerId playerId, Coordinate target) {}
