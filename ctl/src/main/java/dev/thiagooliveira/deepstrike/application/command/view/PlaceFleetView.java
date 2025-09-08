package dev.thiagooliveira.deepstrike.application.command.view;

import dev.thiagooliveira.deepstrike.application.command.model.PlaceFleetViewModel;
import dev.thiagooliveira.deepstrike.infrastructure.client.dto.Coordinate;
import dev.thiagooliveira.deepstrike.infrastructure.client.dto.ShipDeployment;
import java.util.List;

public class PlaceFleetView implements View<PlaceFleetViewModel> {

  private static final String WATER = "\u001B[34m~\u001B[0m"; // azul
  private static final String SHIP = "\u001B[37m█\u001B[0m"; // cinza

  @Override
  public String render(PlaceFleetViewModel model) {
    return FleetBoardRenderer.render(model.ships(), 10);
  }

  static class FleetBoardRenderer {

    public static String render(List<ShipDeployment> ships, int size) {
      char[][] grid = new char[size][size];

      // Inicializa com água
      for (int y = 0; y < size; y++) {
        for (int x = 0; x < size; x++) {
          grid[y][x] = '~';
        }
      }

      // Preenche os navios
      for (ShipDeployment ship : ships) {
        for (Coordinate c : ship.getCoordinates()) {
          grid[c.getY()][c.getX()] = 'S';
        }
      }

      // Monta a string com eixos
      StringBuilder sb = new StringBuilder("Board\n");
      sb.append("   ");
      for (int x = 0; x < size; x++) {
        sb.append((char) ('A' + x)).append(" ");
      }
      sb.append("\n");

      for (int y = 0; y < size; y++) {
        sb.append(String.format("%2d ", y));
        for (int x = 0; x < size; x++) {
          sb.append(colorize(grid[y][x])).append(" ");
        }
        sb.append("\n");
      }

      sb.append("\nFleet placed (").append(ships.size()).append(" ships)!\n");

      return sb.toString();
    }

    private static String colorize(char cell) {
      return switch (cell) {
        case '~' -> WATER;
        case 'S' -> SHIP;
        default -> String.valueOf(cell);
      };
    }
  }
}
