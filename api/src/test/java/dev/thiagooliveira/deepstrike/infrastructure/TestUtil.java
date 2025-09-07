package dev.thiagooliveira.deepstrike.infrastructure;

import dev.thiagooliveira.deepstrike.domain.Coordinate;
import dev.thiagooliveira.deepstrike.domain.event.*;
import dev.thiagooliveira.deepstrike.domain.rule.Ruleset;
import dev.thiagooliveira.deepstrike.domain.ship.Orientation;
import dev.thiagooliveira.deepstrike.domain.ship.Ship;
import dev.thiagooliveira.deepstrike.infrastructure.persistence.eventstore.EventEntity;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestUtil {

  public static final UUID GAME_ID = UUID.randomUUID();
  public static final UUID PLAYER_ID_1 = UUID.randomUUID();
  public static final UUID PLAYER_ID_2 = UUID.randomUUID();

  public static List<EventEntity> eventsForTurnStarted() {
    List<EventEntity> pastEvents = eventsForFleetPlacedForPlayer2();
    var now = Instant.now();
    var entity = createEventEntityType(TurnStarted.class);
    entity.setOccurredAt(now);
    entity.setPayload(
        """
              {
                 "id":{
                    "value":"%s"
                 },
                 "playerId":{
                    "value":"%s"
                 },
                 "occurredAt":%s
              }
              """
            .formatted(GAME_ID, PLAYER_ID_1, now.getEpochSecond()));
    entity.setVersion(pastEvents.getLast().getVersion() + 1);

    List<EventEntity> allEvents = new ArrayList<>(pastEvents);
    allEvents.add(entity);
    return allEvents;
  }

  public static List<EventEntity> eventsForPlayer2Ready() {
    List<EventEntity> pastEvents = eventsForPlayer1Ready();
    var now = Instant.now();
    var entity = createEventEntityType(PlayerReady.class);
    entity.setOccurredAt(now);
    entity.setPayload(
        """
                {
                    "id":{
                       "value":"%s"
                    },
                    "playerId":{
                       "value":"%s"
                    },
                    "occurredAt":%s
                 }
                """
            .formatted(GAME_ID, PLAYER_ID_2, now.getEpochSecond()));
    entity.setVersion(pastEvents.getLast().getVersion() + 1);

    List<EventEntity> allEvents = new ArrayList<>(pastEvents);
    allEvents.add(entity);
    return allEvents;
  }

  private static List<EventEntity> eventsForPlayer1Ready() {
    List<EventEntity> pastEvents = eventsForFleetPlacedForPlayer1();
    var now = Instant.now();
    var entity = createEventEntityType(PlayerReady.class);
    entity.setOccurredAt(now);
    entity.setPayload(
        """
        {
            "id":{
               "value":"%s"
            },
            "playerId":{
               "value":"%s"
            },
            "occurredAt":%s
         }
        """
            .formatted(GAME_ID, PLAYER_ID_1, now.getEpochSecond()));
    entity.setVersion(pastEvents.getLast().getVersion() + 1);

    List<EventEntity> allEvents = new ArrayList<>(pastEvents);
    allEvents.add(entity);
    return allEvents;
  }

  public static List<EventEntity> eventsForFleetPlacedForPlayer2() {
    List<EventEntity> pastEvents = eventsForPlayer1Ready();
    var now = Instant.now();
    var entity = createEventEntityType(FleetPlaced.class);
    entity.setOccurredAt(now);
    entity.setPayload(
        """
                {
                   "id":{
                      "value":"%s"
                   },
                   "playerId":{
                      "value":"%s"
                   },
                   "ships":[
                      {
                         "type":"CARRIER",
                         "bow":{
                            "x":0,
                            "y":0
                         },
                         "orientation":"HORIZONTAL",
                         "size":5,
                         "hits":[

                         ],
                         "footprint":[
                            {
                               "x":0,
                               "y":0
                            },
                            {
                               "x":1,
                               "y":0
                            },
                            {
                               "x":2,
                               "y":0
                            },
                            {
                               "x":3,
                               "y":0
                            },
                            {
                               "x":4,
                               "y":0
                            }
                         ],
                         "sunk":false
                      },
                      {
                         "type":"BATTLESHIP",
                         "bow":{
                            "x":2,
                            "y":1
                         },
                         "orientation":"HORIZONTAL",
                         "size":4,
                         "hits":[

                         ],
                         "footprint":[
                            {
                               "x":2,
                               "y":1
                            },
                            {
                               "x":3,
                               "y":1
                            },
                            {
                               "x":4,
                               "y":1
                            },
                            {
                               "x":5,
                               "y":1
                            }
                         ],
                         "sunk":false
                      },
                      {
                         "type":"DESTROYER",
                         "bow":{
                            "x":4,
                            "y":2
                         },
                         "orientation":"HORIZONTAL",
                         "size":3,
                         "hits":[

                         ],
                         "footprint":[
                            {
                               "x":4,
                               "y":2
                            },
                            {
                               "x":5,
                               "y":2
                            },
                            {
                               "x":6,
                               "y":2
                            }
                         ],
                         "sunk":false
                      },
                      {
                         "type":"SUBMARINE",
                         "bow":{
                            "x":6,
                            "y":3
                         },
                         "orientation":"HORIZONTAL",
                         "size":3,
                         "hits":[

                         ],
                         "footprint":[
                            {
                               "x":6,
                               "y":3
                            },
                            {
                               "x":7,
                               "y":3
                            },
                            {
                               "x":8,
                               "y":3
                            }
                         ],
                         "sunk":false
                      },
                      {
                         "type":"PATROL_BOAT",
                         "bow":{
                            "x":8,
                            "y":4
                         },
                         "orientation":"HORIZONTAL",
                         "size":2,
                         "hits":[

                         ],
                         "footprint":[
                            {
                               "x":8,
                               "y":4
                            },
                            {
                               "x":9,
                               "y":4
                            }
                         ],
                         "sunk":false
                      }
                   ],
                   "occurredAt":%s
                }
                """
            .formatted(GAME_ID, PLAYER_ID_2, now.getEpochSecond()));
    entity.setVersion(pastEvents.getLast().getVersion() + 1);

    List<EventEntity> allEvents = new ArrayList<>(pastEvents);
    allEvents.add(entity);
    return allEvents;
  }

  public static List<EventEntity> eventsForFleetPlacedForPlayer1() {
    List<EventEntity> pastEvents = eventsForPlayerJoined();
    var now = Instant.now();
    var entity = createEventEntityType(FleetPlaced.class);
    entity.setOccurredAt(now);
    entity.setPayload(
        """
                {
                   "id":{
                      "value":"%s"
                   },
                   "playerId":{
                      "value":"%s"
                   },
                   "ships":[
                      {
                         "type":"CARRIER",
                         "bow":{
                            "x":0,
                            "y":0
                         },
                         "orientation":"HORIZONTAL",
                         "size":5,
                         "hits":[

                         ],
                         "footprint":[
                            {
                               "x":0,
                               "y":0
                            },
                            {
                               "x":1,
                               "y":0
                            },
                            {
                               "x":2,
                               "y":0
                            },
                            {
                               "x":3,
                               "y":0
                            },
                            {
                               "x":4,
                               "y":0
                            }
                         ],
                         "sunk":false
                      },
                      {
                         "type":"BATTLESHIP",
                         "bow":{
                            "x":2,
                            "y":1
                         },
                         "orientation":"HORIZONTAL",
                         "size":4,
                         "hits":[

                         ],
                         "footprint":[
                            {
                               "x":2,
                               "y":1
                            },
                            {
                               "x":3,
                               "y":1
                            },
                            {
                               "x":4,
                               "y":1
                            },
                            {
                               "x":5,
                               "y":1
                            }
                         ],
                         "sunk":false
                      },
                      {
                         "type":"DESTROYER",
                         "bow":{
                            "x":4,
                            "y":2
                         },
                         "orientation":"HORIZONTAL",
                         "size":3,
                         "hits":[

                         ],
                         "footprint":[
                            {
                               "x":4,
                               "y":2
                            },
                            {
                               "x":5,
                               "y":2
                            },
                            {
                               "x":6,
                               "y":2
                            }
                         ],
                         "sunk":false
                      },
                      {
                         "type":"SUBMARINE",
                         "bow":{
                            "x":6,
                            "y":3
                         },
                         "orientation":"HORIZONTAL",
                         "size":3,
                         "hits":[

                         ],
                         "footprint":[
                            {
                               "x":6,
                               "y":3
                            },
                            {
                               "x":7,
                               "y":3
                            },
                            {
                               "x":8,
                               "y":3
                            }
                         ],
                         "sunk":false
                      },
                      {
                         "type":"PATROL_BOAT",
                         "bow":{
                            "x":8,
                            "y":4
                         },
                         "orientation":"HORIZONTAL",
                         "size":2,
                         "hits":[

                         ],
                         "footprint":[
                            {
                               "x":8,
                               "y":4
                            },
                            {
                               "x":9,
                               "y":4
                            }
                         ],
                         "sunk":false
                      }
                   ],
                   "occurredAt":%s
                }
                """
            .formatted(GAME_ID, PLAYER_ID_1, now.getEpochSecond()));
    entity.setVersion(pastEvents.getLast().getVersion() + 1);

    List<EventEntity> allEvents = new ArrayList<>(pastEvents);
    allEvents.add(entity);
    return allEvents;
  }

  public static List<EventEntity> eventsForPlayerJoined() {
    var pastEvents = eventsForCreateGame();
    var now = Instant.now();
    var entity = createEventEntityType(PlayerJoined.class);
    entity.setOccurredAt(now);
    entity.setPayload(
        """
              {
                 "id":{
                    "value":"%s"
                 },
                 "joinedPlayerId":{
                    "value":"%s"
                 },
                 "occurredAt":%s
              }
              """
            .formatted(GAME_ID, PLAYER_ID_2, now.getEpochSecond()));
    entity.setVersion(pastEvents.getLast().getVersion() + 1);

    List<EventEntity> allEvents = new ArrayList<>(pastEvents);
    allEvents.add(entity);
    return allEvents;
  }

  public static List<EventEntity> eventsForCreateGame() {
    var now = Instant.now();
    var entity = createEventEntityType(GameCreated.class);
    entity.setOccurredAt(now);
    entity.setPayload(
        """
           {
              "id":{
                 "value":"%s"
              },
              "hostPlayer":{
                 "value":"%s"
              },
              "rules":{
                 "boardSize":10,
                 "fleet":[
                    {
                       "type":"CARRIER",
                       "size":5,
                       "quantity":1
                    },
                    {
                       "type":"BATTLESHIP",
                       "size":4,
                       "quantity":1
                    },
                    {
                       "type":"DESTROYER",
                       "size":3,
                       "quantity":1
                    },
                    {
                       "type":"SUBMARINE",
                       "size":3,
                       "quantity":1
                    },
                    {
                       "type":"PATROL_BOAT",
                       "size":2,
                       "quantity":1
                    }
                 ]
              },
              "occurredAt":%s
           }
           """
            .formatted(GAME_ID, PLAYER_ID_1, now.getEpochSecond()));
    entity.setVersion(entity.getVersion() + 1);
    return List.of(entity);
  }

  public static List<Ship> createFleet() {
    List<Ship> ships = new ArrayList<>();
    int row = 0;
    int y = 0;
    for (Ruleset.ShipSpec spec : Ruleset.formation2002().fleet()) {
      for (int i = 0; i < spec.quantity(); i++) {
        // Places each ship in a different row to avoid collision
        Coordinate start = new Coordinate(row, y);
        Ship ship = new Ship(spec.type(), start, Orientation.HORIZONTAL);
        ships.add(ship);
        row += 2; // leaves space between rows
      }
      y++;
    }

    return ships;
  }

  private static EventEntity createEventEntityType(Class<? extends DomainEvent> clazz) {
    var entity = new EventEntity();
    entity.setAggregateId(GAME_ID);
    entity.setEventType(clazz.getName());
    return entity;
  }
}
