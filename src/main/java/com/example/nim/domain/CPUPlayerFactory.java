package com.example.nim.domain;

import com.example.nim.application.CPUPlayer;

public interface CPUPlayerFactory {
    CPUPlayer getCPUPlayer(CPUDifficulty cpuDifficulty);
}
