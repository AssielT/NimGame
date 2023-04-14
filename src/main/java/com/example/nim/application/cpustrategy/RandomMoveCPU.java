package com.example.nim.application.cpustrategy;

import com.example.nim.application.CPUPlayer;
import com.example.nim.application.GameState;

import java.util.Random;

public class RandomMoveCPU implements CPUPlayer {
    private static final int MAX_REMOVABLE_MATCHES = 3;

    @Override
    public int getNextMove(GameState gameState) {
        Random random = new Random();
        return random.nextInt(Math.min(gameState.matchPile().value(), MAX_REMOVABLE_MATCHES)) + 1;
    }
}
