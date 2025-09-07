package dev.thiagooliveira.deepstrike.application.command;

import dev.thiagooliveira.deepstrike.domain.PlayerId;

public record CreateGameCommand(PlayerId hostPlayerId) {}
