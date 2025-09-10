package dev.thiagooliveira.deepstrike.application.command.view;

import dev.thiagooliveira.deepstrike.application.command.model.GameDetailViewModel;
import dev.thiagooliveira.deepstrike.infrastructure.client.dto.Coordinate;
import dev.thiagooliveira.deepstrike.infrastructure.client.dto.PlayerBoard;
import dev.thiagooliveira.deepstrike.infrastructure.client.dto.ShipPlaced;

public class GameDetailView implements View<GameDetailViewModel> {

  private static final String WATER = "\u001B[34m~\u001B[0m"; // blue
  private static final String SHIP = "\u001B[37m█\u001B[0m"; // gray
  private static final String HIT = "\u001B[31mX\u001B[0m"; // red
  private static final String MISS = "\u001B[36mo\u001B[0m"; // cyan

  @Override
  public String render(GameDetailViewModel model) {
    StringBuilder sb = new StringBuilder();
    sb.append("Game ").append(model.id()).append(" | Status: ").append(model.status()).append("\n");
    sb.append("Created At: ").append(model.createdAt()).append("\n");
    sb.append(model.player1().getId())
        .append(" vs. ")
        .append(
            model.player2() != null && model.player2().getId() != null
                ? model.player2().getId()
                : "-")
        .append("\n");
    sb.append("Turn: ")
        .append(model.currentTurn() != null ? model.currentTurn() : "-")
        .append(" | Winner: ")
        .append(model.winner() != null ? model.winner() : "-")
        .append("\n\n");

    if (model.player1().getBoard() != null
        && model.player2() != null
        && model.player2().getBoard() != null) {
      sb.append(
          BoardRenderer.renderSideBySide(
              model.player1().getId(),
              model.player1().getBoard(),
              model.player2().getId(),
              model.player2().getBoard(),
              model.boardSize()));
    } else if (model.player1().getBoard() != null) {
      sb.append(model.player1().getId())
          .append("'s Board:\n")
          .append(BoardRenderer.renderAsString(model.player1().getBoard(), model.boardSize()))
          .append("\n");
    }

    return sb.toString();
  }

  static class BoardRenderer {

    public static char[][] render(PlayerBoard board, int boardSize) {
      int n = boardSize;
      char[][] grid = new char[n][n];

      // Initialize with water
      for (int y = 0; y < n; y++) {
        for (int x = 0; x < n; x++) {
          grid[y][x] = '~';
        }
      }

      // Only show ships if there's a footprint (player's own board)
      boolean hasFootprints =
          board.getShips().stream()
              .anyMatch(s -> s.getFootprint() != null && !s.getFootprint().isEmpty());
      if (hasFootprints) {
        for (ShipPlaced ship : board.getShips()) {
          for (Coordinate c : ship.getFootprint()) {
            grid[c.getY()][c.getX()] = 'S';
          }
        }
      }

      // Mark received shots
      for (Coordinate shot : board.getShotsReceived()) {
        char current = grid[shot.getY()][shot.getX()];
        if (current == 'S') {
          grid[shot.getY()][shot.getX()] = 'H'; // hit on visible ship
        } else {
          boolean isHit =
              board.getShips().stream()
                  .anyMatch(
                      s ->
                          s.getHits().stream()
                              .anyMatch(h -> h.getX() == shot.getX() && h.getY() == shot.getY()));
          grid[shot.getY()][shot.getX()] = isHit ? 'H' : 'M';
        }
      }

      return grid;
    }

    public static String renderAsString(PlayerBoard board, int boardSize) {
      char[][] grid = render(board, boardSize);
      StringBuilder sb = new StringBuilder();

      int n = grid.length;

      // Header
      sb.append("   ");
      for (int x = 0; x < n; x++) {
        sb.append((char) ('A' + x)).append(' ');
      }
      sb.append("\n");

      for (int y = 0; y < n; y++) {
        sb.append(String.format("%-2d ", y));
        for (int x = 0; x < n; x++) {
          sb.append(colorize(grid[y][x])).append(' ');
        }
        sb.append("\n");
      }

      return sb.toString();
    }

    public static String renderSideBySide(
        String player1Name,
        PlayerBoard board1,
        String player2Name,
        PlayerBoard board2,
        int boardSize) {

      char[][] grid1 = render(board1, boardSize);
      char[][] grid2 = render(board2, boardSize);

      StringBuilder sb = new StringBuilder();

      // Header with names
      sb.append(String.format("%-25s", player1Name + "'s Board"))
          .append("    ")
          .append(player2Name + "'s Board")
          .append("\n");

      // Headers with letters
      sb.append("   ");
      for (int x = 0; x < boardSize; x++) sb.append((char) ('A' + x)).append(' ');
      sb.append("   ");
      sb.append("   ");
      for (int x = 0; x < boardSize; x++) sb.append((char) ('A' + x)).append(' ');
      sb.append("\n");

      // Side-by-side lines
      for (int y = 0; y < boardSize; y++) {
        sb.append(String.format("%-2d ", y));
        for (int x = 0; x < boardSize; x++) {
          sb.append(colorize(grid1[y][x])).append(' ');
        }
        sb.append("   ");
        sb.append(String.format("%-2d ", y));
        for (int x = 0; x < boardSize; x++) {
          sb.append(colorize(grid2[y][x])).append(' ');
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
