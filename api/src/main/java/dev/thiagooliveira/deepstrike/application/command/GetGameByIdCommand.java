package dev.thiagooliveira.deepstrike.application.command;

import dev.thiagooliveira.deepstrike.domain.GameId;

public record GetGameByIdCommand(GameId gameId) {}
