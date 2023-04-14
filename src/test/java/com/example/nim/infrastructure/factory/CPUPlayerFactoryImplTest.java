package com.example.nim.infrastructure.factory;

import com.example.nim.application.CPUPlayer;
import com.example.nim.application.CPUPlayerFactory;
import com.example.nim.application.cpustrategy.RandomMoveCPU;
import com.example.nim.application.cpustrategy.WinningStrategyCPU;
import com.example.nim.domain.CPUDifficulty;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CPUPlayerFactoryImplTest {

    @Test
    public void shouldReturnRandomMoveStrategyOnEasyMode() {
        // given
        CPUPlayerFactory cpuPlayerFactory = new CPUPlayerFactoryImpl();

        // when
        CPUPlayer cpuPlayer = cpuPlayerFactory.getCPUPlayer(CPUDifficulty.EASY);

        // then
        assertThat(cpuPlayer).isInstanceOf(RandomMoveCPU.class);
    }

    @Test
    public void shouldReturnWinningStrategyOnHardMode() {
        // given
        CPUPlayerFactory cpuPlayerFactory = new CPUPlayerFactoryImpl();

        // when
        CPUPlayer cpuPlayer = cpuPlayerFactory.getCPUPlayer(CPUDifficulty.HARD);

        // then
        assertThat(cpuPlayer).isInstanceOf(WinningStrategyCPU.class);
    }

}