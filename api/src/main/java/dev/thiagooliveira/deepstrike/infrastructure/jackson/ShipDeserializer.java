package dev.thiagooliveira.deepstrike.infrastructure.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import dev.thiagooliveira.deepstrike.domain.Coordinate;
import dev.thiagooliveira.deepstrike.domain.ship.Orientation;
import dev.thiagooliveira.deepstrike.domain.ship.Ship;
import dev.thiagooliveira.deepstrike.domain.ship.ShipType;
import java.io.IOException;

public class ShipDeserializer extends StdDeserializer<Ship> {

  public ShipDeserializer() {
    super(Ship.class);
  }

  @Override
  public Ship deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    JsonNode node = p.getCodec().readTree(p);
    ShipType type = ShipType.valueOf(node.get("type").asText());
    JsonNode bowNode = node.get("bow");
    Coordinate bow = new Coordinate(bowNode.get("x").asInt(), bowNode.get("y").asInt());
    Orientation orientation = Orientation.valueOf(node.get("orientation").asText());
    return new Ship(type, bow, orientation);
  }
}
