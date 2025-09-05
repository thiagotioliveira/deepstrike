package com.thiagooliveira.deepstrike.domain.ship;

public enum ShipType {
  CARRIER("Carrier", 5),
  BATTLESHIP("Battleship", 4),
  DESTROYER("Destroyer", 3),
  SUBMARINE("Submarine", 3),
  PATROL_BOAT("Patrol Boat", 2);

  private final String displayName;
  private final int defaultSize;

  ShipType(String displayName, int defaultSize) {
    this.displayName = displayName;
    this.defaultSize = defaultSize;
  }

  public String displayName() {
    return displayName;
  }

  public int defaultSize() {
    return defaultSize;
  }
}
