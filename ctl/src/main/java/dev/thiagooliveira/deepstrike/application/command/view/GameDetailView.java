package dev.thiagooliveira.deepstrike.application.command.view;

import dev.thiagooliveira.deepstrike.application.command.model.GameDetailViewModel;
import dev.thiagooliveira.deepstrike.infrastructure.client.dto.Coordinate;
import dev.thiagooliveira.deepstrike.infrastructure.client.dto.PlayerBoard;
import dev.thiagooliveira.deepstrike.infrastructure.client.dto.ShipPlaced;

public class GameDetailView implements View<GameDetailViewModel> {

  private static final String WATER = "\u001B[34m~\u001B[0m"; // azul
  private static final String SHIP = "\u001B[37m█\u001B[0m"; // cinza
  private static final String HIT = "\u001B[31mX\u001B[0m"; // vermelho
  private static final String MISS = "\u001B[36mo\u001B[0m"; // ciano

  @Override
  public String render(GameDetailViewModel model) {
    StringBuilder sb = new StringBuilder();
    sb.append("Game ").append(model.id()).append(" | Status: ").append(model.status()).append("\n");
    sb.append("Created At: ").append(model.createdAt()).append("\n");
    sb.append(model.player1().getId())
        .append(" vs. ")
        .append(model.player2() != null ? model.player2().getId() : "-")
        .append("\n");
    sb.append("Turn: ")
        .append(model.currentTurn() != null ? model.currentTurn() : "-")
        .append(" | Winner: ")
        .append(model.winner() != null ? model.winner() : "-")
        .append("\n\n");

    sb.append(model.player1().getId())
        .append("'s Board:\n")
        .append(BoardRenderer.renderAsString(model.player1().getBoard()))
        .append("\n");

    if (model.player2() != null) {
      sb.append(model.player2().getId())
          .append("'s Board:\n")
          .append(BoardRenderer.renderAsString(model.player2().getBoard()))
          .append("\n");
    }
    return sb.toString();
  }

  static class BoardRenderer {

    public static char[][] render(PlayerBoard board) {
      int n = board.getSize();
      char[][] grid = new char[n][n];

      // Inicializa com água
      for (int y = 0; y < n; y++) {
        for (int x = 0; x < n; x++) {
          grid[y][x] = '~';
        }
      }

      // Marca navios usando footprint (mais confiável que bow/orientation)
      for (ShipPlaced ship : board.getShips()) {
        for (Coordinate c : ship.getFootprint()) {
          grid[c.getY()][c.getX()] = 'S';
        }
      }

      // Marca tiros recebidos
      for (Coordinate shot : board.getShotsReceived()) {
        char current = grid[shot.getY()][shot.getX()];
        if (current == 'S') {
          grid[shot.getY()][shot.getX()] = 'H'; // acerto
        } else {
          grid[shot.getY()][shot.getX()] = 'M'; // erro
        }
      }

      return grid;
    }

    public static String renderAsString(PlayerBoard board) {
      char[][] grid = render(board);
      StringBuilder sb = new StringBuilder();

      int n = grid.length;

      // Cabeçalho com letras (A, B, C...)
      sb.append("   ");
      for (int x = 0; x < n; x++) {
        sb.append((char) ('A' + x)).append(' ');
      }
      sb.append("\n");

      // Linhas numeradas no eixo Y
      for (int y = 0; y < n; y++) {
        sb.append(String.format("%-2d ", y));
        for (int x = 0; x < n; x++) {
          sb.append(colorize(grid[y][x])).append(' ');
        }
        sb.append("\n");
      }

      return sb.toString();
    }

    private static String colorize(char cell) {
      return switch (cell) {
        case '~' -> WATER;
        case 'S' -> SHIP;
        case 'H' -> HIT;
        case 'M' -> MISS;
        default -> String.valueOf(cell);
      };
    }
  }
}
