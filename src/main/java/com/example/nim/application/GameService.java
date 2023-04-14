package com.example.nim.application;

import com.example.nim.application.exceptions.NimGameNotFoundException;
import com.example.nim.domain.*;
import com.example.nim.domain.exceptions.GameAlreadyEndedException;
import com.example.nim.domain.exceptions.InvalidAmountToRemoveException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameState createGame(CreateGameParameter parameter) throws GameAlreadyEndedException, InvalidAmountToRemoveException {
        MatchPile matchPile = new MatchPile(13);
        GameStatus gameStatus;
        if (parameter.playerOneBegins()) {
            gameStatus = GameStatus.PLAYER_ONE_TURN;
        } else {
            gameStatus = GameStatus.PLAYER_TWO_TURN;
        }
        String gameIdAsString = UUID.randomUUID().toString();
        NimGame game = new NimGame(new GameId(gameIdAsString), matchPile, gameStatus, parameter.difficulty());

        gameRepository.save(game);

        return toGameState(game);
    }

    public GameState takeMatches(GameId gameId, int amount) throws NimGameNotFoundException, GameAlreadyEndedException, InvalidAmountToRemoveException {
        NimGame game = gameRepository.findById(gameId);
        if (game == null) {
            throw new NimGameNotFoundException(gameId);
        }

        game.removeMatches(amount);

        gameRepository.save(game);

        return toGameState(game);
    }

    public GameState getGameState(GameId gameId) throws NimGameNotFoundException {
        NimGame game = gameRepository.findById(gameId);
        if (game == null) {
            throw new NimGameNotFoundException(gameId);
        }

        return toGameState(game);
    }

    private GameState toGameState(NimGame game) {
        return new GameState(game.getGameId(), game.getGameStatus(), game.getMatchPile(), game.getCPUDifficulty());
    }

}
