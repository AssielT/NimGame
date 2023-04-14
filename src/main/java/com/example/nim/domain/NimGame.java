package com.example.nim.domain;

import com.example.nim.domain.exceptions.GameAlreadyEndedException;
import com.example.nim.domain.exceptions.InvalidAmountToRemoveException;

public class NimGame {

    private final GameId gameId;
    private static final int MAX_AMOUNT_REMOVABLE_MATCHES = 3;
    private GameStatus gameStatus;
    private MatchPile matchPile;
    private final CPUDifficulty CPUDifficulty;

    public NimGame(GameId gameId, MatchPile matchPile, GameStatus gameStatus, CPUDifficulty CPUDifficulty) {
        this.gameId = gameId;
        this.matchPile = matchPile;
        this.gameStatus = gameStatus;
        this.CPUDifficulty = CPUDifficulty;
    }

    public CPUDifficulty getCPUDifficulty() {
        return CPUDifficulty;
    }

    public MatchPile getMatchPile() {
        return matchPile;
    }

    public GameId getGameId() {
        return gameId;
    }

    public boolean gameEnded() {
        return gameStatus == GameStatus.PLAYER_ONE_WON || gameStatus == GameStatus.PLAYER_TWO_WON;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public int getMaxAmountOfPossibleRemovableMatches() {
        return Math.min(matchesLeftInPile(), MAX_AMOUNT_REMOVABLE_MATCHES);
    }

    public void removeMatches(int amount) throws GameAlreadyEndedException, InvalidAmountToRemoveException {
        if (gameEnded()) {
            throw new GameAlreadyEndedException(gameId);
        }
        if (!isValidAmountToRemove(amount)) {
            throw new InvalidAmountToRemoveException(amount);
        }

        matchPile = matchPile.removeMatch(amount);

        if (matchPile.isEmpty()) {
            endGame();
        } else {
            changePlayersTurn();
        }
    }

    private void endGame() {
        switch (gameStatus) {
            case PLAYER_ONE_TURN -> gameStatus = GameStatus.PLAYER_TWO_WON;
            case PLAYER_TWO_TURN -> gameStatus = GameStatus.PLAYER_ONE_WON;
        }
    }

    private int matchesLeftInPile() {
        return matchPile.value();
    }

    private boolean isValidAmountToRemove(int amount) {
        return (amount > 0) && (amount <= getMaxAmountOfPossibleRemovableMatches());
    }

    private void changePlayersTurn() {
        if (gameStatus == GameStatus.PLAYER_ONE_TURN) {
            gameStatus = GameStatus.PLAYER_TWO_TURN;
        } else {
            gameStatus = GameStatus.PLAYER_ONE_TURN;
        }
    }
}
