# Nim Game

This is an implementation of the Misere Nim Game against a CPU. You play by calling a REST API

## Requirements

For building and running the application you need

* JDK 17 (make sure your JAVA_HOME is correctly set)

## Running the application locally

You can run the application either with gradle or build a jar file and run it.

### Run with Gradle

```shell
./gradlew bootRun
```

### Run jar file

First create the jar file

```shell
./gradlew build
```

Then run the created jar file located in build/libs

```shell
java -jar <FILENAME>.jar
```

## How to play

The game is automatically played against the CPU. When creating the game you can choose an `EASY` or `HARD` mode.
On `EASY` mode the CPU will just play random moves. On `HARD` mode he plays by using a winning strategy.
If you start the game on `HARD` mode and choose to begin, there is no way you can win as you are already in a loosing
position, due to starting the game with 13 matches.

After each move the current game state will be returned. The game state included the game id, the current status of the
game and the matches left. The status can show the players turn or the winner of the game.
Valid Values are `PLAYER_ONE_TURN`,`PLAYER_TWO_TURN`,`PLAYER_ONE_WON`,`PLAYER_TWO_WON`.
Although `PLAYER_TWO_TURN` is a valid response it will never be shown, because after each of your turns the cpu will
make its move and the players turn will change back to player one.

### Start a Game

If you let the CPU begin, the response will have a different amount of matches left. This is because the CPU
will already have made its move. So the response will include the matches left after the CPUs turn.

```
POST /misereGame
Accept: application/json
Content-Type: application/json

{
"difficulty": "HARD", 
"playerOneBegins":"true"
}

RESPONSE: HTTP 200 (Ok)
{"gameId":"8dc5f865-3994-4cdc-b069-9e0ba9614cf3","status":"PLAYER_ONE_TURN","matchesLeft":13}
```

### Remove Match

When you call this endpoint you will remove matches from the pile. After that the CPU will automatically make
its move and also remove matches. So the response again wont show the amount left after your move but after yours
and the CPUs.

```
POST /misereGame/{id}
Accept: application/json
Content-Type: application/json

{
"matchesToRemove": 2 
}

RESPONSE: HTTP 200 (Ok)
{"gameId":"8dc5f865-3994-4cdc-b069-9e0ba9614cf3","status":"PLAYER_ONE_TURN","matchesLeft":10}
```

### Get Game State

```
GET /misereGame/{id}

RESPONSE: HTTP 200 (Ok)
{"gameId":"8dc5f865-3994-4cdc-b069-9e0ba9614cf3","status":"PLAYER_ONE_TURN","matchesLeft":10}
```