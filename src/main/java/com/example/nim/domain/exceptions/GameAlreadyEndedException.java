package com.example.nim.domain.exceptions;

import com.example.nim.domain.GameId;

public class GameAlreadyEndedException extends Exception {
    public GameAlreadyEndedException(GameId gameId) {
        super("The game with the id '" + gameId + "' already ended");
    }
}
