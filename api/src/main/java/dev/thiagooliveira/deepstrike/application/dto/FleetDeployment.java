package dev.thiagooliveira.deepstrike.application.dto;

import dev.thiagooliveira.deepstrike.domain.Coordinate;
import java.util.List;

public record FleetDeployment(List<ShipDeployment> ships) {

  public record ShipDeployment(String type, List<Coordinate> coordinates) {}
}
