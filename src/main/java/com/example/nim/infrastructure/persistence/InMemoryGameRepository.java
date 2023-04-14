package com.example.nim.infrastructure.persistence;

import com.example.nim.domain.GameId;
import com.example.nim.domain.GameRepository;
import com.example.nim.domain.NimGame;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryGameRepository implements GameRepository {
    private final Map<GameId, NimGame> data = new HashMap<>();

    @Override
    public void save(NimGame game) {
        data.put(game.getGameId(), game);
    }

    @Override
    public NimGame findById(GameId gameId) {
        return data.get(gameId);
    }
}
