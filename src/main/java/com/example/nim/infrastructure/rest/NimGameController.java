package com.example.nim.infrastructure.rest;

import com.example.nim.application.CreateGameParameter;
import com.example.nim.application.GameService;
import com.example.nim.application.GameState;
import com.example.nim.application.exceptions.NimGameNotFoundException;
import com.example.nim.domain.CPUDifficulty;
import com.example.nim.domain.CPUPlayerFactory;
import com.example.nim.domain.GameId;
import com.example.nim.domain.GameStatus;
import com.example.nim.domain.exceptions.GameAlreadyEndedException;
import com.example.nim.domain.exceptions.InvalidAmountToRemoveException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class NimGameController {

    private final GameService gameService;
    private final CPUPlayerFactory cpuPlayerFactory;

    public NimGameController(GameService gameService, CPUPlayerFactory cpuPlayerFactory) {
        this.gameService = gameService;
        this.cpuPlayerFactory = cpuPlayerFactory;
    }

    @PostMapping("/misereGame")
    public GameStateResource startGame(@Valid @RequestBody CreateGameParametersResource parameters) {
        try {
            GameState gameState = gameService.createGame(toCreateGameParameter(parameters));

            if (!parameters.isPlayerOneBegins()) {
                gameState = cpuMove(gameState);
            }
            return toGameStateResource(gameState);
        } catch (GameAlreadyEndedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e); // // this exception should never be reached
        } catch (InvalidAmountToRemoveException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (NimGameNotFoundException e) {
            throw new RuntimeException(e); // this exception should never be reached
        }
    }

    @PostMapping("/misereGame/{id}")
    public GameStateResource takeMatches(@PathVariable String id, @Valid @RequestBody RemoveMatchesResource removeMatchesResource) {
        try {
            GameState gameState = gameService.takeMatches(new GameId(id), removeMatchesResource.getMatchesToRemove());
            if (gameState.gameStatus() == GameStatus.PLAYER_TWO_WON) {
                return toGameStateResource(gameState);
            }

            gameState = cpuMove(gameState);
            return toGameStateResource(gameState);
        } catch (NimGameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (GameAlreadyEndedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (InvalidAmountToRemoveException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

    @GetMapping("/misereGame/{id}")
    public GameStateResource getGameState(@PathVariable String id) {
        try {
            return toGameStateResource(gameService.getGameState(new GameId(id)));
        } catch (NimGameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    private GameStateResource toGameStateResource(GameState gameState) {
        return new GameStateResource(gameState.gameId().value(), GameStatusResource.valueOf(gameState.gameStatus().toString()), gameState.matchPile().value());
    }

    private CreateGameParameter toCreateGameParameter(CreateGameParametersResource createGameParametersResource) {
        return new CreateGameParameter(CPUDifficulty.valueOf(createGameParametersResource.getDifficulty().toString()), createGameParametersResource.isPlayerOneBegins());
    }

    private GameState cpuMove(GameState gameState) throws GameAlreadyEndedException, InvalidAmountToRemoveException, NimGameNotFoundException {
        int matchesToRemove = cpuPlayerFactory.getCPUPlayer(gameState.cpuDifficulty()).getNextMove(gameState);
        return gameService.takeMatches(gameState.gameId(), matchesToRemove);
    }
}
