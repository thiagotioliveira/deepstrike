package dev.thiagooliveira.deepstrike.domain.ship;

import dev.thiagooliveira.deepstrike.domain.Coordinate;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Ship implements Serializable {
  private final ShipType type;
  private final Coordinate bow; // initial coordinate (bow)
  private final Orientation orientation;
  private final int size;
  private final Set<Coordinate> hits = new HashSet<>();

  private final List<Coordinate> footprint;

  public Ship(ShipType type, Coordinate bow, Orientation orientation) {
    this.type = type;
    this.bow = bow;
    this.orientation = orientation;
    this.size = type.defaultSize();
    this.footprint = calculateFootprint();
  }

  private List<Coordinate> calculateFootprint() {
    return IntStream.range(0, size)
        .mapToObj(
            i ->
                orientation == Orientation.HORIZONTAL
                    ? new Coordinate(bow.x() + i, bow.y())
                    : new Coordinate(bow.x(), bow.y() + i))
        .collect(Collectors.toList());
  }

  /** Registers a hit on the ship. */
  public void registerHit(Coordinate target) {
    if (footprint.contains(target)) {
      hits.add(target);
    }
  }

  /** Returns whether the ship has been sunk. */
  public boolean isSunk() {
    return hits.containsAll(footprint);
  }

  /** Returns whether the ship would be sunk if this coordinate is hit, without mutating state. */
  public boolean isSunkAfterHit(Coordinate target) {
    Set<Coordinate> hypotheticalHits = new HashSet<>(hits);
    if (footprint.contains(target)) {
      hypotheticalHits.add(target);
    }
    return hypotheticalHits.containsAll(footprint);
  }

  public ShipType getType() {
    return type;
  }

  public Coordinate getBow() {
    return bow;
  }

  public Orientation getOrientation() {
    return orientation;
  }

  public int getSize() {
    return size;
  }

  public List<Coordinate> getFootprint() {
    return footprint;
  }

  public Set<Coordinate> getHits() {
    return hits;
  }
}
