package dev.thiagooliveira.deepstrike.domain;

import java.util.List;
import java.util.Set;

public record CurrentFleet(List<ShipDeployment> ships) {

  public record ShipDeployment(String type, List<Coordinate> coordinates, Set<Coordinate> hits) {}
}
