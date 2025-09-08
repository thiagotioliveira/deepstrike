package dev.thiagooliveira.deepstrike.application.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.thiagooliveira.deepstrike.infrastructure.client.dto.Coordinate;
import dev.thiagooliveira.deepstrike.infrastructure.client.dto.Ship;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class FleetGenerator {

  private final ObjectMapper objectMapper;

  public FleetGenerator(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public List<Ship> generateFromRandom() {
    List<Ship> ships = new ArrayList<>();
    Random random = new Random();

    // Definindo os tipos de navios (tamanho típico de Batalha Naval)
    Map<Ship.TypeEnum, Integer> shipSizes =
        Map.of(
            Ship.TypeEnum.CARRIER, 5,
            Ship.TypeEnum.BATTLESHIP, 4,
            Ship.TypeEnum.DESTROYER, 3,
            Ship.TypeEnum.SUBMARINE, 3,
            Ship.TypeEnum.PATROL_BOAT, 2);

    int boardSize = 10; // NxN (podes parametrizar depois)

    for (var entry : shipSizes.entrySet()) {
      Ship.TypeEnum type = entry.getKey();
      int length = entry.getValue();

      boolean placed = false;
      while (!placed) {
        // Sorteia orientação
        Ship.OrientationEnum orientation =
            random.nextBoolean() ? Ship.OrientationEnum.HORIZONTAL : Ship.OrientationEnum.VERTICAL;

        // Sorteia posição válida dentro do tabuleiro
        int x = random.nextInt(boardSize);
        int y = random.nextInt(boardSize);

        // Ajusta coordenadas para não sair do board
        if (orientation == Ship.OrientationEnum.HORIZONTAL && y + length > boardSize) {
          y = boardSize - length;
        }
        if (orientation == Ship.OrientationEnum.VERTICAL && x + length > boardSize) {
          x = boardSize - length;
        }

        Coordinate bow = new Coordinate().x(x).y(y);

        // (💡 opcional) aqui poderias verificar sobreposição com outros ships

        ships.add(new Ship().type(type).orientation(orientation).bow(bow));
        placed = true;
      }
    }
    return ships;
  }

  public List<Ship> generateFromFile(String fleetFilePath) {
    try {
      if (!Files.exists(Path.of(fleetFilePath))) {
        throw new IllegalStateException("Fleet file not found at " + fleetFilePath);
      }
      String json = Files.readString(Path.of(fleetFilePath));
      List<Map<String, Object>> fleet = objectMapper.readValue(json, new TypeReference<>() {});
      List<Ship> ships = new ArrayList<>();
      for (Map<String, Object> ship : fleet) {
        ships.add(
            new Ship()
                .orientation(
                    ship.get("orientation").equals("HORIZONTAL")
                        ? Ship.OrientationEnum.HORIZONTAL
                        : Ship.OrientationEnum.VERTICAL)
                .type(
                    switch ((String) ship.get("type")) {
                      case "BATTLESHIP" -> Ship.TypeEnum.BATTLESHIP;
                      case "CARRIER" -> Ship.TypeEnum.CARRIER;
                      case "PATROL_BOAT" -> Ship.TypeEnum.PATROL_BOAT;
                      case "DESTROYER" -> Ship.TypeEnum.DESTROYER;
                      case "SUBMARINE" -> Ship.TypeEnum.SUBMARINE;
                      default ->
                          throw new IllegalArgumentException(
                              "Unknown ship type: " + ship.get("type"));
                    })
                .bow(new Coordinate().x((int) ship.get("x")).y((int) ship.get("y"))));
      }
      return ships;
    } catch (IOException e) {
      throw new RuntimeException("Failed to read fleet file", e);
    }
  }
}
