package com.example.nim.application.exceptions;

import com.example.nim.domain.GameId;

public class NimGameNotFoundException extends Exception {
    public NimGameNotFoundException(GameId gameId) {
        super("Game with the id '" + gameId + "' not found");
    }
}
