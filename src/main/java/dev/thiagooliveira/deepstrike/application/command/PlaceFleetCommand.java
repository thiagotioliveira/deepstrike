package dev.thiagooliveira.deepstrike.application.command;

import dev.thiagooliveira.deepstrike.domain.GameId;
import dev.thiagooliveira.deepstrike.domain.PlayerId;
import dev.thiagooliveira.deepstrike.domain.ship.Ship;
import java.util.List;

public record PlaceFleetCommand(GameId gameId, PlayerId playerId, List<Ship> ships) {}
