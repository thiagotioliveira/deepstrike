package dev.thiagooliveira.deepstrike.application.command.model;

import dev.thiagooliveira.deepstrike.infrastructure.client.dto.ShipDeployment;
import java.util.List;

public record PlaceFleetViewModel(List<ShipDeployment> ships) {}
