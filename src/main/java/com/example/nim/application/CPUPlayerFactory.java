package com.example.nim.application;

import com.example.nim.domain.CPUDifficulty;

public interface CPUPlayerFactory {
    CPUPlayer getCPUPlayer(CPUDifficulty cpuDifficulty);
}
