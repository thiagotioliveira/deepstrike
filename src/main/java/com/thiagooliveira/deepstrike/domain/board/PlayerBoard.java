package com.thiagooliveira.deepstrike.domain.board;

import com.thiagooliveira.deepstrike.domain.Coordinate;
import com.thiagooliveira.deepstrike.domain.ship.Ship;
import java.util.*;

public class PlayerBoard {
  private final int size; // NxN
  private final List<Ship> ships = new ArrayList<>();
  private final Set<Coordinate> shotsReceived = new HashSet<>();

  public PlayerBoard(int size) {
    if (size < 5 || size > 20) throw new IllegalArgumentException("Board size invalid");
    this.size = size;
  }

  public void placeFleet(List<Ship> fleet) {
    if (!ships.isEmpty()) {
      throw new IllegalStateException("Fleet already placed");
    }
    validateFleet(fleet);
    ships.addAll(fleet);
  }

  /** Registers a shot from the opponent. Returns shot result (MISS, HIT or SUNK). */
  public ShotResult registerShot(Coordinate target) {
    if (!isWithinBounds(target)) {
      throw new IllegalArgumentException("Shot out of bounds");
    }
    if (shotsReceived.contains(target)) {
      throw new IllegalStateException("Coordinate already shot");
    }

    shotsReceived.add(target);

    for (Ship ship : ships) {
      if (ship.getFootprint().contains(target)) {
        ship.registerHit(target);
        return ship.isSunk() ? ShotResult.SUNK : ShotResult.HIT;
      }
    }

    return ShotResult.MISS;
  }

  /** Checks if all ships have been sunk. */
  public boolean allShipsSunk() {
    return ships.stream().allMatch(Ship::isSunk);
  }

  public int getSize() {
    return size;
  }

  public List<Ship> getShips() {
    return Collections.unmodifiableList(ships);
  }

  public Set<Coordinate> getShotsReceived() {
    return Collections.unmodifiableSet(shotsReceived);
  }

  /** Validates fleet: board boundaries and overlapping. */
  private void validateFleet(List<Ship> fleet) {
    Set<Coordinate> occupied = new HashSet<>();

    for (Ship ship : fleet) {
      for (Coordinate c : ship.getFootprint()) {
        if (!isWithinBounds(c)) {
          throw new IllegalArgumentException("Ship out of bounds: " + ship.getType());
        }
        if (!occupied.add(c)) {
          throw new IllegalArgumentException("Ships overlapping at " + c);
        }
      }
    }
  }

  private boolean isWithinBounds(Coordinate c) {
    return c.x() >= 0 && c.x() < size && c.y() >= 0 && c.y() < size;
  }
}
