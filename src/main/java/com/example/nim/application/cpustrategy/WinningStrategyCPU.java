package com.example.nim.application.cpustrategy;

import com.example.nim.application.CPUPlayer;
import com.example.nim.application.GameState;

public class WinningStrategyCPU implements CPUPlayer {

    @Override
    public int getNextMove(GameState gameState) {
        int matchesLeft = gameState.matchPile().value();
        if (isLoosingPosition(matchesLeft)) {
            return 1; // you are loosing anyway
        }
        return (matchesLeft + 3) % 4;
    }

    private boolean isLoosingPosition(int matchesLeft) {
        return matchesLeft % 4 == 1;
    }
}
