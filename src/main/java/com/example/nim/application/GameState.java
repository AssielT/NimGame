package com.example.nim.application;

import com.example.nim.domain.CPUDifficulty;
import com.example.nim.domain.GameId;
import com.example.nim.domain.GameStatus;
import com.example.nim.domain.MatchPile;

public record GameState(GameId gameId, GameStatus gameStatus, MatchPile matchPile, CPUDifficulty cpuDifficulty) {
}
