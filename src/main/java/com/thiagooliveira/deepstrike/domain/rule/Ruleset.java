package com.thiagooliveira.deepstrike.domain.rule;

import com.thiagooliveira.deepstrike.domain.ship.ShipType;
import java.util.List;
import java.util.Objects;

/** Value Object that defines the rules of a match. */
public record Ruleset(int boardSize, List<ShipSpec> fleet) {

  public Ruleset {
    if (boardSize < 5 || boardSize > 20) {
      throw new IllegalArgumentException("Board size must be between 5 and 20");
    }
    Objects.requireNonNull(fleet, "Fleet cannot be null");
    if (fleet.isEmpty()) {
      throw new IllegalArgumentException("Fleet cannot be empty");
    }
  }

  /** Specification of a ship type allowed in the game. */
  public record ShipSpec(ShipType type, int size, int quantity) {
    public ShipSpec {
      Objects.requireNonNull(type, "ShipType cannot be null");
      if (size <= 0) throw new IllegalArgumentException("Ship size must be > 0");
      if (quantity <= 0) throw new IllegalArgumentException("Ship quantity must be > 0");
    }
  }

  /** Classic rules (Wikipedia). */
  public static Ruleset formation2002() {
    return new Ruleset(
        10,
        List.of(
            new ShipSpec(ShipType.CARRIER, 5, 1),
            new ShipSpec(ShipType.BATTLESHIP, 4, 1),
            new ShipSpec(ShipType.DESTROYER, 3, 1),
            new ShipSpec(ShipType.SUBMARINE, 3, 1),
            new ShipSpec(ShipType.PATROL_BOAT, 2, 1)));
  }
}
