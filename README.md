# Deep Strike API

> Monorepo for **Deep Strike**, a Battleship-inspired game for **2 players**

---

## 🗂️ Project Structure

This project is organized as a **multi-module Maven build**:

- **`api/`** → REST API exposing the game endpoints
- **`spec/`** → OpenAPI specification (used to generate server and client stubs)
- **`ctl/`** → Command-line client (Shell) to interact with the API

---

## 🎮 About the Game



The game is played on four grids, two for each player. The grids are typically square, usually 10×10, and the individual squares in the grid are identified by letter and number. On one grid the player arranges ships and records the shots by the opponent. On the other grid, the player records their own shots.

Before play begins, each player secretly arranges their ships on their primary grid. Each ship occupies a number of consecutive squares on the grid, arranged either horizontally or vertically. The number of squares for each ship is determined by the type of ship. The ships cannot overlap (i.e., only one ship can occupy any given square in the grid) or be placed diagonally. The types and numbers of ships allowed are the same for each player. These may vary depending on the rules. The ships should be hidden from the opposing player's sight and players are not allowed to see each other's pieces. The game is a discovery game in which players need to discover their opponent's ship positions.

The rules specify the following ships:

| No. | Class of ship | Size |
| --- | --- | --- |
| 1 | Carrier | 5 |
| 2 | Battleship | 4 |
| 3 | Destroyer | 3 |
| 4 | Submarine | 3 |
| 5 | Patrol Boat | 2 |

After the ships have been positioned, the game proceeds in a series of rounds. In each round, each player takes a turn to announce a target square in the opponent's grid which is to be shot at. The opponent announces whether or not the square is occupied by a ship. If it is a "hit", the player who is hit marks this on their own "ocean" or grid (with a red peg in the pegboard version), and announces what ship was hit. The attacking player marks the hit or miss on their own "tracking" or "target" grid with a pencil marking in the paper version of the game, or the appropriate color peg in the pegboard version (red for "hit", white for "miss"), in order to build up a picture of the opponent's fleet.

When all of the squares of a ship have been hit, the ship's owner announces the sinking of the Carrier, Submarine, Cruiser/Destroyer/Patrol Boat, or the titular Battleship. If all of a player's ships have been sunk, the game is over and their opponent wins.

For more, check [Wikipedia](https://en.wikipedia.org/wiki/Battleship_(game))

---

## 🚀 Getting Started

### Prerequisites
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
- [Docker](https://www.docker.com/)
- [Maven](https://maven.apache.org/) (or use the included Maven Wrapper `./mvnw`)

### Build All Modules

```bash
./mvnw clean install
```

### Run API Server

```bash
cd api/
../mvnw spring-boot:run
```

API available at:
👉 http://localhost:8080

Swagger available at:
👉 http://localhost:8080/swagger-ui/index.html

### Run Client (Shell)

```bash
cd ctl/
../mvnw spring-boot:run
```