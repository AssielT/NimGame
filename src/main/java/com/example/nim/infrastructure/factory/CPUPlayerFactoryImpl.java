package com.example.nim.infrastructure.factory;

import com.example.nim.application.CPUPlayer;
import com.example.nim.application.cpustrategy.RandomMoveCPU;
import com.example.nim.application.cpustrategy.WinningStrategyCPU;
import com.example.nim.domain.CPUDifficulty;
import com.example.nim.domain.CPUPlayerFactory;
import org.springframework.stereotype.Component;

@Component
public class CPUPlayerFactoryImpl implements CPUPlayerFactory {
    @Override
    public CPUPlayer getCPUPlayer(CPUDifficulty cpuDifficulty) {
        return switch (cpuDifficulty) {
            case EASY -> new RandomMoveCPU();
            case HARD -> new WinningStrategyCPU();
        };
    }
}
