package com.example.nim.application;

import com.example.nim.domain.CPUDifficulty;

public record CreateGameParameter(CPUDifficulty difficulty, boolean playerOneBegins) {
}
