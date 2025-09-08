package dev.thiagooliveira.deepstrike.domain;

import java.util.List;

public record FleetDeployment(List<ShipDeployment> ships) {

  public record ShipDeployment(String type, List<Coordinate> coordinates) {}
}
